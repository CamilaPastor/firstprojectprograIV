import { useEffect, useState } from 'react'
import { api } from '../../lib/api.js'

export default function AdminCaracteristicas() {
  const [actualId, setActualId] = useState(null)
  const [actualNombre, setActualNombre] = useState(null)
  const [parents, setParents] = useState([])
  const [lista, setLista] = useState([])
  const [nombre, setNombre] = useState('')
  const [descripcion, setDescripcion] = useState('')
  const [error, setError] = useState(null)
  const [msg, setMsg] = useState(null)

  function cargar(id) {
    const url = id ? `/api/admin/caracteristicas?idPadre=${id}` : '/api/admin/caracteristicas'
    api.get(url).then(setLista).catch(e => setError(e.message))
  }
  useEffect(() => { cargar(actualId) }, [actualId])

  function entrar(c) {
    setParents(p => [...p, { id: actualId, nombre: actualNombre }])
    setActualId(c.idCaracteristica)
    setActualNombre(c.nombre)
  }
  function volver() {
    const prev = parents[parents.length - 1]
    setParents(p => p.slice(0, -1))
    setActualId(prev?.id ?? null)
    setActualNombre(prev?.nombre ?? null)
  }

  async function crear(e) {
    e.preventDefault()
    setError(null); setMsg(null)
    try {
      await api.post('/api/admin/caracteristicas', { nombre, descripcion, idPadre: actualId })
      setMsg('Creada')
      setNombre(''); setDescripcion('')
      cargar(actualId)
    } catch (e) { setError(e.message) }
  }

  async function eliminar(id) {
    if (!confirm('Eliminar esta caracteristica?')) return
    setError(null); setMsg(null)
    try {
      const r = await api.del(`/api/admin/caracteristicas/${id}`)
      setMsg(r.message)
      cargar(actualId)
    } catch (e) { setError(e.message) }
  }

  return (
    <div>
      <h1 className="page-title">Caracteristicas {actualNombre && <span className="muted">/ {actualNombre}</span>}</h1>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="grid" style={{ gridTemplateColumns: '2fr 1fr' }}>
        <div className="card">
          <div className="flex-between" style={{ marginBottom: '0.75rem' }}>
            <strong>{actualNombre ? `Hijas de "${actualNombre}"` : 'Raices'}</strong>
            {parents.length > 0 && <button className="btn btn-sm btn-secondary" onClick={volver}>← Volver</button>}
          </div>
          {lista.length === 0 ? (
            <div className="muted">Sin caracteristicas en este nivel.</div>
          ) : (
            <table>
              <thead><tr><th>Nombre</th><th>Descripcion</th><th></th></tr></thead>
              <tbody>
                {lista.map(c => (
                  <tr key={c.idCaracteristica}>
                    <td>{c.nombre}</td>
                    <td className="muted">{c.descripcion || '-'}</td>
                    <td>
                      <button className="btn btn-sm" onClick={() => entrar(c)}>Entrar</button>{' '}
                      <button className="btn btn-sm btn-danger" onClick={() => eliminar(c.idCaracteristica)}>Eliminar</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Nueva {actualId ? 'subcaracteristica' : 'raiz'}</h3>
          <form onSubmit={crear}>
            <div className="form-row"><label>Nombre *</label><input value={nombre} onChange={e => setNombre(e.target.value)} required /></div>
            <div className="form-row"><label>Descripcion</label><textarea value={descripcion} onChange={e => setDescripcion(e.target.value)} /></div>
            <button className="btn">Crear</button>
          </form>
        </div>
      </div>
    </div>
  )
}
