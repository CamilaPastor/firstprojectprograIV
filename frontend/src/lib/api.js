const TOKEN_KEY = 'bolsa.token'
const USER_KEY = 'bolsa.user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setSession(token, user) {
  localStorage.setItem(TOKEN_KEY, token)
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function getUser() {
  const raw = localStorage.getItem(USER_KEY)
  return raw ? JSON.parse(raw) : null
}

export function clearSession() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

async function request(path, { method = 'GET', body, isMultipart = false, raw = false } = {}) {
  const headers = {}
  const token = getToken()
  if (token) headers['Authorization'] = `Bearer ${token}`

  let fetchBody
  if (body && isMultipart) {
    fetchBody = body
  } else if (body !== undefined) {
    headers['Content-Type'] = 'application/json'
    fetchBody = JSON.stringify(body)
  }

  const res = await fetch(path, { method, headers, body: fetchBody })

  if (res.status === 401) {
    clearSession()
    if (!path.includes('/api/auth/')) {
      window.location.href = '/login'
    }
  }

  if (raw) return res

  const contentType = res.headers.get('content-type') || ''
  const data = contentType.includes('application/json') ? await res.json() : await res.text()

  if (!res.ok) {
    const message = (data && data.message) ? data.message : (typeof data === 'string' ? data : 'Error en la solicitud')
    const err = new Error(message)
    err.status = res.status
    err.data = data
    throw err
  }
  return data
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: 'POST', body }),
  put: (path, body) => request(path, { method: 'PUT', body }),
  del: (path) => request(path, { method: 'DELETE' }),
  upload: (path, formData) => request(path, { method: 'POST', body: formData, isMultipart: true }),
  raw: (path) => request(path, { raw: true })
}
