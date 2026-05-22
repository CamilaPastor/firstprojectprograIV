import { createContext, useContext, useState } from 'react'
import { getUser, setSession, clearSession } from '../lib/api.js'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(getUser())

  function login(token, userData) {
    setSession(token, userData)
    setUser(userData)
  }

  function logout() {
    clearSession()
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
