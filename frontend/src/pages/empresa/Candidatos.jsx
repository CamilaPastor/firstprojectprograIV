import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api } from '../../lib/api.js'

export default function Candidatos() {
  const { idPuesto } = useParams()
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get(`/api/empresa/puestos/${idPuesto}/candidatos`).then(setData).catch(e => setError(e.message))
  }, [idPuesto])

  if (error) return <div className="alert alert-danger">{error}</div>
  if (!data) return <div>Cargando...</div>

  return (
    <div>
      <h1 className="page-title">Candidatos</h1>
      {data.puesto && (
        <div className="card">
          <h3 style={{ marginTop: 0 }}>{data.puesto.descripcion?.slice(0, 80)}</h3>
          <div className="tags">
            {data.puesto.caracteristicas?.map(c => <span className="tag" key={c.idCaracteristica}>{c.nombre} (nivel {c.nivelRequerido})</span>)}
          </div>
        </div>
      )}
      {data.candidatos.length === 0 ? (
        <div className="empty-state">No hay oferentes activos para comparar.</div>
      ) : (
        <div className="card" style={{ padding: 0 }}>
          <table>
            <thead>
              <tr>
                <th>Identificacion</th>
                <th>Nombre</th>
                <th>Cumple</th>
                <th>%</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {data.candidatos.map(c => (
                <tr key={c.idOferente}>
                  <td>{c.identificacion}</td>
                  <td>{c.nombre} {c.apellido}</td>
                  <td>{c.cumplidos}/{c.total}</td>
                  <td>{c.porcentaje?.toFixed(1)}%</td>
                  <td><Link className="btn btn-sm" to={`/empresa/oferente/${c.idOferente}`}>Ver detalle</Link></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
