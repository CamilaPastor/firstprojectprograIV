import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../../lib/api.js'

export default function MisPuestos() {
  const [puestos, setPuestos] = useState([])
  const [error, setError] = useState(null)
  const [msg, setMsg] = useState(null)

  function cargar() {
    api.get('/api/empresa/puestos').then(setPuestos).catch(e => setError(e.message))
  }
  useEffect(cargar, [])

  async function desactivar(id) {
    setError(null); setMsg(null)
    try {
      const r = await api.post(`/api/empresa/puestos/${id}/desactivar`)
      setMsg(r.message)
      cargar()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <div>
      <div className="flex-between">
        <h1 className="page-title">Mis puestos</h1>
        <Link className="btn btn-success" to="/empresa/nuevo-puesto">Nuevo puesto</Link>
      </div>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      {puestos.length === 0 ? (
        <div className="empty-state">Aun no has publicado puestos.</div>
      ) : (
        <div className="card" style={{ padding: 0 }}>
          <table>
            <thead>
              <tr>
                <th>Descripcion</th>
                <th>Salario</th>
                <th>Tipo</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {puestos.map(p => (
                <tr key={p.idPuesto}>
                  <td>{p.descripcion?.slice(0, 80)}</td>
                  <td>{p.salario ? `₡ ${Number(p.salario).toLocaleString('es-CR')}` : '-'}</td>
                  <td>{p.tipoPublicacion}</td>
                  <td>{p.activo ? <span className="tag">Activo</span> : <span className="muted">Inactivo</span>}</td>
                  <td>
                    <Link className="btn btn-sm" to={`/empresa/candidatos/${p.idPuesto}`}>Candidatos</Link>{' '}
                    {p.activo && <button className="btn btn-sm btn-danger" onClick={() => desactivar(p.idPuesto)}>Desactivar</button>}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
