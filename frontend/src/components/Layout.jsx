import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

export default function Layout({ children }) {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/')
  }

  return (
    <div className="app-shell">
      <header className="navbar">
        <div className="navbar-inner">
          <Link to="/" className="brand">Bolsa de Empleo</Link>
          <nav className="nav-links">
            <Link to="/">Inicio</Link>
            <Link to="/buscar">Buscar puestos</Link>
            {!user && <Link to="/login">Iniciar sesion</Link>}
            {!user && <Link to="/registro/empresa">Registro empresa</Link>}
            {!user && <Link to="/registro/oferente">Registro oferente</Link>}
            {user?.role === 'admin' && <Link to="/admin/dashboard">Admin</Link>}
            {user?.role === 'empresa' && <Link to="/empresa/dashboard">Empresa</Link>}
            {user?.role === 'oferente' && <Link to="/oferente/dashboard">Oferente</Link>}
            {user && (
              <>
                <span className="user-tag">{user.nombre} ({user.role})</span>
                <button className="btn-link" onClick={handleLogout}>Salir</button>
              </>
            )}
          </nav>
        </div>
      </header>
      <main className="container">{children}</main>
      <footer className="footer">EIF209 Programacion IV - Proyecto II</footer>
    </div>
  )
}
