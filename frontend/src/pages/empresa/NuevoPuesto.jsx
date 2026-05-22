import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../../lib/api.js'

function NodoSeleccion({ node, niveles, toggle, setNivel }) {
  const selected = niveles[node.idCaracteristica] !== undefined
  return (
    <li>
      <div className="tree-row">
        <label className="tree-label">
          <input type="checkbox" checked={selected} onChange={() => toggle(node.idCaracteristica)} />
          <span>{node.nombre}</span>
        </label>
        {selected && (
          <select value={niveles[node.idCaracteristica]} onChange={e => setNivel(node.idCaracteristica, Number(e.target.value))} style={{ marginLeft: '0.5rem', padding: '2px 6px' }}>
            {[1,2,3,4,5].map(n => <option key={n} value={n}>nivel {n}</option>)}
          </select>
        )}
      </div>
      {node.hijos && node.hijos.length > 0 && (
        <ul className="tree-children">
          {node.hijos.map(h => <NodoSeleccion key={h.idCaracteristica} node={h} niveles={niveles} toggle={toggle} setNivel={setNivel} />)}
        </ul>
      )}
    </li>
  )
}

export default function NuevoPuesto() {
  const navigate = useNavigate()
  const [arbol, setArbol] = useState([])
  const [descripcion, setDescripcion] = useState('')
  const [salario, setSalario] = useState('')
  const [tipoPublicacion, setTipoPublicacion] = useState('publico')
  const [niveles, setNiveles] = useState({})
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    api.get('/api/public/caracteristicas/arbol').then(setArbol).catch(e => setError(e.message))
  }, [])

  function toggle(id) {
    setNiveles(n => {
      if (n[id] !== undefined) {
        const copy = { ...n }; delete copy[id]; return copy
      }
      return { ...n, [id]: 3 }
    })
  }
  function setNivel(id, v) { setNiveles(n => ({ ...n, [id]: v })) }

  async function submit(e) {
    e.preventDefault()
    setError(null); setLoading(true)
    try {
      const caracteristicas = Object.entries(niveles).map(([idCaracteristica, nivelRequerido]) => ({
        idCaracteristica: Number(idCaracteristica), nivelRequerido
      }))
      await api.post('/api/empresa/puestos', {
        descripcion,
        salario: salario ? Number(salario) : null,
        tipoPublicacion,
        caracteristicas
      })
      navigate('/empresa/mis-puestos')
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <h1 className="page-title">Nuevo puesto</h1>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit}>
        <div className="grid" style={{ gridTemplateColumns: '2fr 1fr' }}>
          <div className="card">
            <div className="form-row"><label>Descripcion *</label><textarea value={descripcion} onChange={e => setDescripcion(e.target.value)} required /></div>
            <div className="form-row"><label>Salario (colones)</label><input type="number" value={salario} onChange={e => setSalario(e.target.value)} /></div>
            <div className="form-row">
              <label>Tipo de publicacion</label>
              <select value={tipoPublicacion} onChange={e => setTipoPublicacion(e.target.value)}>
                <option value="publico">Publico</option>
                <option value="privado">Privado (solo oferentes)</option>
              </select>
            </div>
          </div>
          <div className="card">
            <h3 style={{ marginTop: 0 }}>Caracteristicas requeridas</h3>
            <ul className="tree">
              {arbol.map(n => <NodoSeleccion key={n.idCaracteristica} node={n} niveles={niveles} toggle={toggle} setNivel={setNivel} />)}
            </ul>
          </div>
        </div>
        <button className="btn" type="submit" disabled={loading}>{loading ? 'Publicando...' : 'Publicar puesto'}</button>
      </form>
    </div>
  )
}
