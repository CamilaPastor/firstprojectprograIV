import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../../lib/api.js'

export default function AdminDashboard() {
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get('/api/admin/dashboard').then(setData).catch(e => setError(e.message))
  }, [])

  return (
    <div>
      <h1 className="page-title">Panel de administracion</h1>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="stat-grid">
        <div className="stat"><div className="label">Empresas pendientes</div><div className="value">{data?.empresasPendientes ?? '...'}</div></div>
        <div className="stat"><div className="label">Oferentes pendientes</div><div className="value">{data?.oferentesPendientes ?? '...'}</div></div>
        <div className="stat"><div className="label">Caracteristicas</div><div className="value">{data?.totalCaracteristicas ?? '...'}</div></div>
      </div>
      <div className="card" style={{ marginTop: '1rem' }}>
        <h3 style={{ marginTop: 0 }}>Acciones</h3>
        <div className="inline-form">
          <Link className="btn" to="/admin/empresas-pendientes">Empresas pendientes</Link>
          <Link className="btn" to="/admin/oferentes-pendientes">Oferentes pendientes</Link>
          <Link className="btn" to="/admin/caracteristicas">Caracteristicas</Link>
          <Link className="btn btn-secondary" to="/admin/reportes">Reportes</Link>
        </div>
      </div>
    </div>
  )
}
