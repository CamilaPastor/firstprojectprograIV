import { useEffect, useState } from 'react'
import { api, getToken } from '../../lib/api.js'

export default function MiCv() {
  const [info, setInfo] = useState(null)
  const [archivo, setArchivo] = useState(null)
  const [error, setError] = useState(null)
  const [msg, setMsg] = useState(null)
  const [loading, setLoading] = useState(false)

  function cargar() {
    api.get('/api/oferente/cv').then(setInfo).catch(e => setError(e.message))
  }
  useEffect(cargar, [])

  async function subir(e) {
    e.preventDefault()
    if (!archivo) { setError('Selecciona un archivo PDF'); return }
    setError(null); setMsg(null); setLoading(true)
    try {
      const fd = new FormData()
      fd.append('archivo', archivo)
      const r = await api.upload('/api/oferente/cv', fd)
      setMsg(r.message)
      cargar()
    } catch (e) { setError(e.message) } finally { setLoading(false) }
  }

  async function descargar() {
    const res = await fetch('/api/oferente/cv/descargar', { headers: { Authorization: `Bearer ${getToken()}` } })
    if (!res.ok) { setError('No se pudo descargar'); return }
    const blob = await res.blob()
    const url = URL.createObjectURL(blob)
    window.open(url, '_blank')
  }

  return (
    <div>
      <h1 className="page-title">Mi CV</h1>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="card">
        {info?.tieneCv ? (
          <>
            <p><strong>Archivo:</strong> {info.nombreArchivo}</p>
            <p className="muted">Subido: {info.fechaSubida}</p>
            <button className="btn" onClick={descargar}>Descargar CV actual</button>
          </>
        ) : (
          <div className="muted">No has subido un CV todavia.</div>
        )}
      </div>
      <div className="card">
        <h3 style={{ marginTop: 0 }}>{info?.tieneCv ? 'Reemplazar' : 'Subir'} CV (PDF)</h3>
        <form onSubmit={subir}>
          <div className="form-row">
            <input type="file" accept="application/pdf" onChange={e => setArchivo(e.target.files[0])} required />
          </div>
          <button className="btn" type="submit" disabled={loading}>{loading ? 'Subiendo...' : 'Subir CV'}</button>
        </form>
      </div>
    </div>
  )
}
