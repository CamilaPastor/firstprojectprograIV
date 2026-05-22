import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../../lib/api.js'
import { useAuth } from '../../context/AuthContext.jsx'

export default function EmpresaDashboard() {
  const { user } = useAuth()
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get('/api/empresa/dashboard').then(setData).catch(e => setError(e.message))
  }, [])

  return (
    <div>
      <h1 className="page-title">Bienvenido, {user?.nombre}</h1>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="stat-grid">
        <div className="stat">
          <div className="label">Puestos activos</div>
          <div className="value">{data?.puestosActivos ?? '...'}</div>
        </div>
      </div>
      <div className="card" style={{ marginTop: '1rem' }}>
        <h3 style={{ marginTop: 0 }}>Acciones</h3>
        <div className="inline-form">
          <Link className="btn" to="/empresa/mis-puestos">Ver mis puestos</Link>
          <Link className="btn btn-success" to="/empresa/nuevo-puesto">Publicar nuevo puesto</Link>
        </div>
      </div>
    </div>
  )
}
