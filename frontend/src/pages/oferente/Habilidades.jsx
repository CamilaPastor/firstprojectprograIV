import { useEffect, useState } from 'react'
import { api } from '../../lib/api.js'

function Nodo({ node, habilidades, agregar, nivel, setNivel }) {
  const tiene = habilidades.find(h => h.idCaracteristica === node.idCaracteristica)
  return (
    <li>
      <div className="tree-row">
        <span>{node.nombre}</span>
        {tiene ? (
          <span className="tag" style={{ marginLeft: '0.5rem' }}>Tienes (nivel {tiene.nivel})</span>
        ) : (
          <>
            <select value={nivel[node.idCaracteristica] || 3} onChange={e => setNivel(node.idCaracteristica, Number(e.target.value))} style={{ marginLeft: '0.5rem', padding: '2px 6px' }}>
              {[1,2,3,4,5].map(n => <option key={n} value={n}>nivel {n}</option>)}
            </select>
            <button className="btn btn-sm" onClick={() => agregar(node.idCaracteristica)} style={{ marginLeft: '0.4rem' }}>Agregar</button>
          </>
        )}
      </div>
      {node.hijos && node.hijos.length > 0 && (
        <ul className="tree-children">
          {node.hijos.map(h => <Nodo key={h.idCaracteristica} node={h} habilidades={habilidades} agregar={agregar} nivel={nivel} setNivel={setNivel} />)}
        </ul>
      )}
    </li>
  )
}

export default function Habilidades() {
  const [habilidades, setHabilidades] = useState([])
  const [arbol, setArbol] = useState([])
  const [niveles, setNiveles] = useState({})
  const [error, setError] = useState(null)
  const [msg, setMsg] = useState(null)

  function cargar() {
    Promise.all([
      api.get('/api/oferente/habilidades'),
      api.get('/api/oferente/caracteristicas/arbol')
    ]).then(([hs, ar]) => { setHabilidades(hs); setArbol(ar) }).catch(e => setError(e.message))
  }
  useEffect(cargar, [])

  async function agregar(idCaracteristica) {
    setError(null); setMsg(null)
    try {
      const nivel = niveles[idCaracteristica] || 3
      const r = await api.post('/api/oferente/habilidades', { idCaracteristica, nivel })
      setMsg(r.message)
      cargar()
    } catch (e) { setError(e.message) }
  }

  async function eliminar(idCaracteristica) {
    setError(null); setMsg(null)
    try {
      const r = await api.del(`/api/oferente/habilidades/${idCaracteristica}`)
      setMsg(r.message)
      cargar()
    } catch (e) { setError(e.message) }
  }

  return (
    <div>
      <h1 className="page-title">Mis habilidades</h1>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="grid" style={{ gridTemplateColumns: '1fr 1fr' }}>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Habilidades actuales</h3>
          {habilidades.length === 0 ? (
            <div className="muted">Aun no agregas habilidades.</div>
          ) : (
            <ul style={{ paddingLeft: '1rem' }}>
              {habilidades.map(h => (
                <li key={h.id} style={{ marginBottom: '0.4rem' }}>
                  <strong>{h.nombreCaracteristica}</strong> (nivel {h.nivel})
                  <button className="btn btn-sm btn-danger" onClick={() => eliminar(h.idCaracteristica)} style={{ marginLeft: '0.5rem' }}>Quitar</button>
                </li>
              ))}
            </ul>
          )}
        </div>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Catalogo de caracteristicas</h3>
          <ul className="tree">
            {arbol.map(n => <Nodo key={n.idCaracteristica} node={n} habilidades={habilidades} agregar={agregar} nivel={niveles} setNivel={(id, v) => setNiveles(x => ({ ...x, [id]: v }))} />)}
          </ul>
        </div>
      </div>
    </div>
  )
}
