import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../lib/api.js'

export default function RegistroOferente() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    identificacion: '', nombre: '', apellido: '', nacionalidad: '',
    telefono: '', correo: '', residencia: '', password: ''
  })
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
      const res = await api.post('/api/auth/registro/oferente', form)
      navigate('/login', { state: { success: res.message } })
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: 560, margin: '0 auto' }}>
      <h1 className="page-title">Registro de oferente</h1>
      <p className="page-subtitle">Tu cuenta quedara pendiente de aprobacion por el administrador.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      <form className="card" onSubmit={handleSubmit}>
        <div className="form-row"><label>Identificacion *</label><input value={form.identificacion} onChange={e => update('identificacion', e.target.value)} required /></div>
        <div className="form-row"><label>Nombre *</label><input value={form.nombre} onChange={e => update('nombre', e.target.value)} required /></div>
        <div className="form-row"><label>Apellido *</label><input value={form.apellido} onChange={e => update('apellido', e.target.value)} required /></div>
        <div className="form-row"><label>Nacionalidad</label><input value={form.nacionalidad} onChange={e => update('nacionalidad', e.target.value)} /></div>
        <div className="form-row"><label>Telefono</label><input value={form.telefono} onChange={e => update('telefono', e.target.value)} /></div>
        <div className="form-row"><label>Correo *</label><input type="email" value={form.correo} onChange={e => update('correo', e.target.value)} required /></div>
        <div className="form-row"><label>Residencia</label><input value={form.residencia} onChange={e => update('residencia', e.target.value)} /></div>
        <div className="form-row"><label>Contrasena *</label><input type="password" value={form.password} onChange={e => update('password', e.target.value)} required minLength={4} /></div>
        <button className="btn" type="submit" disabled={loading}>{loading ? 'Enviando...' : 'Registrar oferente'}</button>
      </form>
    </div>
  )
}
