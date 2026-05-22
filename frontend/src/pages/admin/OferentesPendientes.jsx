import { useEffect, useState } from 'react'
import { api } from '../../lib/api.js'

export default function OferentesPendientes() {
  const [oferentes, setOferentes] = useState([])
  const [error, setError] = useState(null)
  const [msg, setMsg] = useState(null)

  function cargar() {
    api.get('/api/admin/oferentes-pendientes').then(setOferentes).catch(e => setError(e.message))
  }
  useEffect(cargar, [])

  async function aprobar(id) {
    setError(null); setMsg(null)
    try {
      const r = await api.post(`/api/admin/oferentes/${id}/aprobar`)
      setMsg(r.message)
      cargar()
    } catch (e) { setError(e.message) }
  }

  return (
    <div>
      <h1 className="page-title">Oferentes pendientes</h1>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      {oferentes.length === 0 ? (
        <div className="empty-state">No hay oferentes pendientes.</div>
      ) : (
        <div className="card" style={{ padding: 0 }}>
          <table>
            <thead><tr><th>Identificacion</th><th>Nombre</th><th>Correo</th><th>Fecha</th><th></th></tr></thead>
            <tbody>
              {oferentes.map(o => (
                <tr key={o.idOferente}>
                  <td>{o.identificacion}</td>
                  <td>{o.nombre} {o.apellido}</td>
                  <td>{o.correo}</td>
                  <td>{o.fechaRegistro}</td>
                  <td><button className="btn btn-sm btn-success" onClick={() => aprobar(o.idOferente)}>Aprobar</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
