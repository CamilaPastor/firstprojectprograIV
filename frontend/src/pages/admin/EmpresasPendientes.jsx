import { useEffect, useState } from 'react'
import { api } from '../../lib/api.js'

export default function EmpresasPendientes() {
  const [empresas, setEmpresas] = useState([])
  const [error, setError] = useState(null)
  const [msg, setMsg] = useState(null)

  function cargar() {
    api.get('/api/admin/empresas-pendientes').then(setEmpresas).catch(e => setError(e.message))
  }
  useEffect(cargar, [])

  async function aprobar(id) {
    setError(null); setMsg(null)
    try {
      const r = await api.post(`/api/admin/empresas/${id}/aprobar`)
      setMsg(r.message)
      cargar()
    } catch (e) { setError(e.message) }
  }

  return (
    <div>
      <h1 className="page-title">Empresas pendientes</h1>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      {empresas.length === 0 ? (
        <div className="empty-state">No hay empresas pendientes.</div>
      ) : (
        <div className="card" style={{ padding: 0 }}>
          <table>
            <thead><tr><th>Nombre</th><th>Correo</th><th>Localizacion</th><th>Fecha</th><th></th></tr></thead>
            <tbody>
              {empresas.map(e => (
                <tr key={e.idEmpresa}>
                  <td>{e.nombre}</td>
                  <td>{e.correo}</td>
                  <td>{e.localizacion || '-'}</td>
                  <td>{e.fechaRegistro}</td>
                  <td><button className="btn btn-sm btn-success" onClick={() => aprobar(e.idEmpresa)}>Aprobar</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
