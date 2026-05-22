import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../../lib/api.js'
import { useAuth } from '../../context/AuthContext.jsx'

export default function OferenteDashboard() {
  const { user } = useAuth()
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get('/api/oferente/dashboard').then(setData).catch(e => setError(e.message))
  }, [])

  return (
    <div>
      <h1 className="page-title">Hola, {user?.nombre}</h1>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="stat-grid">
        <div className="stat">
          <div className="label">Mis habilidades</div>
          <div className="value">{data?.totalHabilidades ?? '...'}</div>
        </div>
        <div className="stat">
          <div className="label">CV</div>
          <div className="value">{data?.tieneCv ? 'Subido' : 'Falta'}</div>
        </div>
      </div>
      <div className="card" style={{ marginTop: '1rem' }}>
        <h3 style={{ marginTop: 0 }}>Acciones</h3>
        <div className="inline-form">
          <Link className="btn" to="/oferente/habilidades">Gestionar habilidades</Link>
          <Link className="btn" to="/oferente/mi-cv">Mi CV</Link>
          <Link className="btn btn-secondary" to="/buscar">Buscar puestos</Link>
        </div>
      </div>
    </div>
  )
}
