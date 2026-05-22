import { Routes, Route, Navigate } from 'react-router-dom'
import Layout from './components/Layout.jsx'
import PrivateRoute from './components/PrivateRoute.jsx'

import Inicio from './pages/Inicio.jsx'
import Login from './pages/Login.jsx'
import RegistroEmpresa from './pages/RegistroEmpresa.jsx'
import RegistroOferente from './pages/RegistroOferente.jsx'
import BuscarPuestos from './pages/BuscarPuestos.jsx'

import AdminDashboard from './pages/admin/Dashboard.jsx'
import EmpresasPendientes from './pages/admin/EmpresasPendientes.jsx'
import OferentesPendientes from './pages/admin/OferentesPendientes.jsx'
import AdminCaracteristicas from './pages/admin/Caracteristicas.jsx'
import AdminReportes from './pages/admin/Reportes.jsx'

import EmpresaDashboard from './pages/empresa/Dashboard.jsx'
import MisPuestos from './pages/empresa/MisPuestos.jsx'
import NuevoPuesto from './pages/empresa/NuevoPuesto.jsx'
import Candidatos from './pages/empresa/Candidatos.jsx'
import DetalleCandidato from './pages/empresa/DetalleCandidato.jsx'

import OferenteDashboard from './pages/oferente/Dashboard.jsx'
import Habilidades from './pages/oferente/Habilidades.jsx'
import MiCv from './pages/oferente/MiCv.jsx'

export default function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Inicio />} />
        <Route path="/login" element={<Login />} />
        <Route path="/registro/empresa" element={<RegistroEmpresa />} />
        <Route path="/registro/oferente" element={<RegistroOferente />} />
        <Route path="/buscar" element={<BuscarPuestos />} />

        <Route path="/admin" element={<Navigate to="/admin/dashboard" replace />} />
        <Route path="/admin/dashboard" element={<PrivateRoute role="admin"><AdminDashboard /></PrivateRoute>} />
        <Route path="/admin/empresas-pendientes" element={<PrivateRoute role="admin"><EmpresasPendientes /></PrivateRoute>} />
        <Route path="/admin/oferentes-pendientes" element={<PrivateRoute role="admin"><OferentesPendientes /></PrivateRoute>} />
        <Route path="/admin/caracteristicas" element={<PrivateRoute role="admin"><AdminCaracteristicas /></PrivateRoute>} />
        <Route path="/admin/reportes" element={<PrivateRoute role="admin"><AdminReportes /></PrivateRoute>} />

        <Route path="/empresa" element={<Navigate to="/empresa/dashboard" replace />} />
        <Route path="/empresa/dashboard" element={<PrivateRoute role="empresa"><EmpresaDashboard /></PrivateRoute>} />
        <Route path="/empresa/mis-puestos" element={<PrivateRoute role="empresa"><MisPuestos /></PrivateRoute>} />
        <Route path="/empresa/nuevo-puesto" element={<PrivateRoute role="empresa"><NuevoPuesto /></PrivateRoute>} />
        <Route path="/empresa/candidatos/:idPuesto" element={<PrivateRoute role="empresa"><Candidatos /></PrivateRoute>} />
        <Route path="/empresa/oferente/:idOferente" element={<PrivateRoute role="empresa"><DetalleCandidato /></PrivateRoute>} />

        <Route path="/oferente" element={<Navigate to="/oferente/dashboard" replace />} />
        <Route path="/oferente/dashboard" element={<PrivateRoute role="oferente"><OferenteDashboard /></PrivateRoute>} />
        <Route path="/oferente/habilidades" element={<PrivateRoute role="oferente"><Habilidades /></PrivateRoute>} />
        <Route path="/oferente/mi-cv" element={<PrivateRoute role="oferente"><MiCv /></PrivateRoute>} />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Layout>
  )
}
