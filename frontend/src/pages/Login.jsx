import { useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { api } from '../lib/api.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [usuario, setUsuario] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const successMsg = location.state?.success

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const data = await api.post('/api/auth/login', { usuario, password })
      login(data.token, { id: data.id, nombre: data.nombre, correo: data.correo, role: data.role })
      const targets = { admin: '/admin/dashboard', empresa: '/empresa/dashboard', oferente: '/oferente/dashboard' }
      navigate(targets[data.role] || '/')
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: 420, margin: '0 auto' }}>
      <h1 className="page-title">Iniciar sesion</h1>
      {successMsg && <div className="alert alert-success">{successMsg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <form className="card" onSubmit={handleSubmit}>
        <div className="form-row">
          <label>Usuario o correo</label>
          <input value={usuario} onChange={e => setUsuario(e.target.value)} required autoFocus />
        </div>
        <div className="form-row">
          <label>Contrasena</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </div>
        <button className="btn" type="submit" disabled={loading}>{loading ? 'Ingresando...' : 'Ingresar'}</button>
      </form>
    </div>
  )
}
