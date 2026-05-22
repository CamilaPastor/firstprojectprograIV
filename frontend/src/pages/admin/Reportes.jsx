import { useState } from 'react'
import { getToken } from '../../lib/api.js'

export default function AdminReportes() {
  const ahora = new Date()
  const [anio, setAnio] = useState(ahora.getFullYear())
  const [mes, setMes] = useState(ahora.getMonth() + 1)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  async function descargar(e) {
    e.preventDefault()
    setError(null); setLoading(true)
    try {
      const res = await fetch(`/api/admin/reportes/mensual?anio=${anio}&mes=${mes}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
      })
      if (!res.ok) throw new Error('No se pudo generar el reporte')
      const blob = await res.blob()
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `Reporte_Puestos_${anio}_${String(mes).padStart(2,'0')}.pdf`
      a.click()
      URL.revokeObjectURL(url)
    } catch (e) { setError(e.message) } finally { setLoading(false) }
  }

  return (
    <div>
      <h1 className="page-title">Reportes</h1>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="card" style={{ maxWidth: 420 }}>
        <h3 style={{ marginTop: 0 }}>Reporte mensual de puestos</h3>
        <form onSubmit={descargar}>
          <div className="form-row">
            <label>Anio</label>
            <input type="number" value={anio} onChange={e => setAnio(Number(e.target.value))} min="2020" max="2100" required />
          </div>
          <div className="form-row">
            <label>Mes</label>
            <select value={mes} onChange={e => setMes(Number(e.target.value))}>
              {Array.from({ length: 12 }, (_, i) => i + 1).map(m => <option key={m} value={m}>{m}</option>)}
            </select>
          </div>
          <button className="btn" disabled={loading}>{loading ? 'Generando...' : 'Descargar PDF'}</button>
        </form>
      </div>
    </div>
  )
}
