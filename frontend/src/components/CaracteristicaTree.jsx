import { useState } from 'react'

export default function CaracteristicaTree({ nodes, selectedIds, onToggle }) {
  if (!nodes || nodes.length === 0) return <p className="muted">Sin caracteristicas disponibles</p>
  return (
    <ul className="tree">
      {nodes.map(n => (
        <TreeNode key={n.idCaracteristica} node={n} selectedIds={selectedIds} onToggle={onToggle} />
      ))}
    </ul>
  )
}

function TreeNode({ node, selectedIds, onToggle }) {
  const [open, setOpen] = useState(true)
  const hasChildren = node.hijos && node.hijos.length > 0
  const checked = selectedIds.includes(node.idCaracteristica)
  return (
    <li>
      <div className="tree-row">
        {hasChildren ? (
          <button type="button" className="tree-toggle" onClick={() => setOpen(o => !o)}>{open ? '▾' : '▸'}</button>
        ) : <span className="tree-toggle-empty" />}
        <label className="tree-label">
          <input type="checkbox" checked={checked} onChange={() => onToggle(node.idCaracteristica)} />
          <span>{node.nombre}</span>
        </label>
      </div>
      {hasChildren && open && (
        <ul className="tree-children">
          {node.hijos.map(h => (
            <TreeNode key={h.idCaracteristica} node={h} selectedIds={selectedIds} onToggle={onToggle} />
          ))}
        </ul>
      )}
    </li>
  )
}
