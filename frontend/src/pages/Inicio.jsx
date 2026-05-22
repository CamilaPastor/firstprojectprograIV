import { useEffect, useState } from 'react'
import { api } from '../lib/api.js'

export default function Inicio() {
  const [puestos, setPuestos] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get('/api/public/puestos/recientes')
      .then(setPuestos)
      .catch(e => setError(e.message))
  }, [])

  return (
    <div>
      <h1 className="page-title">Puestos recientes</h1>
      <p className="page-subtitle">Los 5 puestos publicos mas recientes. Pasa el cursor para ver el detalle.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      {puestos.length === 0 && !error && <div className="empty-state">Aun no hay puestos publicados.</div>}
      <div className="grid grid-2">
        {puestos.map(p => (
          <article className="puesto-card" key={p.idPuesto}>
            <h3>{p.descripcion?.split('\n')[0]?.slice(0, 60) || 'Puesto'}</h3>
            <div className="empresa">{p.nombreEmpresa}</div>
            {p.salario && <div className="salario">₡ {Number(p.salario).toLocaleString('es-CR')}</div>}
            <div className="tags">
              {p.caracteristicas?.slice(0, 4).map(c => (
                <span className="tag" key={c.idCaracteristica}>{c.nombre}</span>
              ))}
            </div>
            <div className="hover-detail">
              <h3>{p.nombreEmpresa}</h3>
              <p style={{ whiteSpace: 'pre-wrap' }}>{p.descripcion}</p>
              {p.salario && <p><strong>Salario:</strong> ₡ {Number(p.salario).toLocaleString('es-CR')}</p>}
              <div>
                {p.caracteristicas?.map(c => (
                  <span className="tag" key={c.idCaracteristica}>{c.nombre} (nivel {c.nivelRequerido})</span>
                ))}
              </div>
            </div>
          </article>
        ))}
      </div>
    </div>
  )
}
