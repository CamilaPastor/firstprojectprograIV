import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { api, getToken } from '../../lib/api.js'

export default function DetalleCandidato() {
  const { idOferente } = useParams()
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get(`/api/empresa/oferentes/${idOferente}`).then(setData).catch(e => setError(e.message))
  }, [idOferente])

  async function descargarCv() {
    const res = await fetch(`/api/empresa/oferentes/${idOferente}/cv`, {
      headers: { Authorization: `Bearer ${getToken()}` }
    })
    if (!res.ok) { setError('No se pudo descargar el CV'); return }
    const blob = await res.blob()
    const url = URL.createObjectURL(blob)
    window.open(url, '_blank')
  }

  if (error) return <div className="alert alert-danger">{error}</div>
  if (!data) return <div>Cargando...</div>

  const o = data.oferente
  return (
    <div>
      <h1 className="page-title">{o.nombre} {o.apellido}</h1>
      <div className="card">
        <p><strong>Identificacion:</strong> {o.identificacion}</p>
        <p><strong>Correo:</strong> {o.correo}</p>
        <p><strong>Nacionalidad:</strong> {o.nacionalidad || '-'}</p>
        <p><strong>Residencia:</strong> {o.residencia || '-'}</p>
        <p><strong>Telefono:</strong> {o.telefono || '-'}</p>
      </div>
      <div className="card">
        <h3 style={{ marginTop: 0 }}>Habilidades</h3>
        {data.habilidades.length === 0 ? (
          <div className="muted">Sin habilidades registradas.</div>
        ) : (
          <div>
            {data.habilidades.map(h => <span className="tag" key={h.id}>{h.nombreCaracteristica} (nivel {h.nivel})</span>)}
          </div>
        )}
      </div>
      <div className="card">
        <h3 style={{ marginTop: 0 }}>CV</h3>
        {data.tieneCv ? <button className="btn" onClick={descargarCv}>Descargar CV</button> : <div className="muted">Este oferente no ha subido CV.</div>}
      </div>
    </div>
  )
}
