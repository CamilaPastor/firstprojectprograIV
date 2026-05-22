import { useEffect, useState } from 'react'
import { api } from '../lib/api.js'
import { useAuth } from '../context/AuthContext.jsx'
import CaracteristicaTree from '../components/CaracteristicaTree.jsx'

export default function BuscarPuestos() {
  const { user } = useAuth()
  const esOferente = user?.role === 'oferente'
  const base = esOferente ? '/api/oferente' : '/api/public'

  const [arbol, setArbol] = useState([])
  const [seleccion, setSeleccion] = useState([])
  const [puestos, setPuestos] = useState([])
  const [error, setError] = useState(null)
  const [buscando, setBuscando] = useState(false)

  useEffect(() => {
    api.get(`${base}/caracteristicas/arbol`)
      .then(setArbol)
      .catch(e => setError(e.message))
  }, [base])

  function toggle(id) {
    setSeleccion(s => s.includes(id) ? s.filter(x => x !== id) : [...s, id])
  }

  async function buscar() {
    if (seleccion.length === 0) {
      setPuestos([])
      return
    }
    setError(null)
    setBuscando(true)
    try {
      const qs = seleccion.map(id => `ids=${id}`).join('&')
      const data = await api.get(`${base}/puestos/buscar?` + qs)
      setPuestos(data)
    } catch (e) {
      setError(e.message)
    } finally {
      setBuscando(false)
    }
  }

  return (
    <div>
      <h1 className="page-title">Buscar puestos por caracteristicas</h1>
      <p className="page-subtitle">
        {esOferente
          ? 'Incluye puestos publicos y privados disponibles para oferentes.'
          : 'Mostrando solo puestos publicos. Inicia sesion como oferente para ver tambien los privados.'}
      </p>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="grid" style={{ gridTemplateColumns: 'minmax(260px, 320px) 1fr' }}>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Caracteristicas</h3>
          <CaracteristicaTree nodes={arbol} selectedIds={seleccion} onToggle={toggle} />
          <button className="btn" onClick={buscar} disabled={buscando} style={{ marginTop: '0.75rem' }}>
            {buscando ? 'Buscando...' : `Buscar (${seleccion.length})`}
          </button>
        </div>
        <div>
          {puestos.length === 0 ? (
            <div className="empty-state">Selecciona caracteristicas y presiona Buscar.</div>
          ) : (
            <div className="grid grid-2">
              {puestos.map(p => (
                <article className="puesto-card" key={p.idPuesto}>
                  {p.tipoPublicacion === 'privado' && <span className="badge badge-privado">Privado</span>}
                  <h3>{p.descripcion?.split('\n')[0]?.slice(0, 60)}</h3>
                  <div className="empresa">{p.nombreEmpresa}</div>
                  {p.salario && <div className="salario">₡ {Number(p.salario).toLocaleString('es-CR')}</div>}
                  <div className="tags">
                    {p.caracteristicas?.map(c => <span className="tag" key={c.idCaracteristica}>{c.nombre}</span>)}
                  </div>
                </article>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
