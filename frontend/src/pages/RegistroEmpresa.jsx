import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../lib/api.js'

export default function RegistroEmpresa() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ nombre: '', localizacion: '', correo: '', telefono: '', descripcion: '', password: '' })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  function update(field, value) {
    setForm(f => ({ ...f, [field]: value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await api.post('/api/auth/registro/empresa', form)
      navigate('/login', { state: { success: res.message } })
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: 560, margin: '0 auto' }}>
      <h1 className="page-title">Registro de empresa</h1>
      <p className="page-subtitle">Tu cuenta quedara pendiente de aprobacion por el administrador.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      <form className="card" onSubmit={handleSubmit}>
        <div className="form-row"><label>Nombre *</label><input value={form.nombre} onChange={e => update('nombre', e.target.value)} required /></div>
        <div className="form-row"><label>Correo *</label><input type="email" value={form.correo} onChange={e => update('correo', e.target.value)} required /></div>
        <div className="form-row"><label>Localizacion</label><input value={form.localizacion} onChange={e => update('localizacion', e.target.value)} /></div>
        <div className="form-row"><label>Telefono</label><input value={form.telefono} onChange={e => update('telefono', e.target.value)} /></div>
        <div className="form-row"><label>Descripcion</label><textarea value={form.descripcion} onChange={e => update('descripcion', e.target.value)} /></div>
        <div className="form-row"><label>Contrasena *</label><input type="password" value={form.password} onChange={e => update('password', e.target.value)} required minLength={4} /></div>
        <button className="btn" type="submit" disabled={loading}>{loading ? 'Enviando...' : 'Registrar empresa'}</button>
      </form>
    </div>
  )
}
