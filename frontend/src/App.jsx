import { useState, useEffect, useRef } from 'react'
import { Client } from '@stomp/stompjs'

export default function App() {
  const [token, setToken] = useState(null)
  const [username, setUsername] = useState('')
  const [connected, setConnected] = useState(false)
  const [messages, setMessages] = useState([])
  const [to, setTo] = useState('')
  const [msg, setMsg] = useState('')
  const clientRef = useRef(null)

  const authenticate = async (mode, u, p) => {
    const res = await fetch(`/auth/${mode}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: u, password: p })
    })
    const body = await res.text()
    if (!res.ok) {
      alert(`${mode} failed: ${body || res.status}`)
      return
    }
    if (mode === 'register') {
      alert('Registered. Now login.')
      return { registered: true }
    }
    setToken(body)
    setUsername(u)
  }

  const logout = () => {
    clientRef.current?.deactivate()
    setToken(null)
    setUsername('')
    setConnected(false)
    setMessages([])
  }

  useEffect(() => {
    if (!token) return
    const client = new Client({
      brokerURL: `ws://${window.location.hostname}:8080/stomp`,
      connectHeaders: { Authorization: 'Bearer ' + token },
      reconnectDelay: 5000
    })
    client.onConnect = () => {
      setConnected(true)
      client.subscribe('/user/queue/message', m => addMsg('PRIVATE', m.body))
      client.subscribe('/topic/message', m => addMsg('BROADCAST', m.body))
    }
    client.onDisconnect = () => setConnected(false)
    client.onStompError = f => addMsg('ERROR', f.headers.message || 'STOMP error')
    client.activate()
    clientRef.current = client
    return () => { client.deactivate() }
  }, [token])

  const addMsg = (kind, body) => {
    setMessages(prev => [...prev, { kind, body, time: new Date().toLocaleTimeString() }])
  }

  const sendPrivate = () => {
    if (!msg || !to) return
    clientRef.current.publish({
      destination: '/app/private',
      body: JSON.stringify({ message: msg, from: username, to })
    })
    setMsg('')
  }

  const sendBroadcast = () => {
    if (!msg) return
    clientRef.current.publish({
      destination: '/app/message',
      body: JSON.stringify({ message: msg, from: username, to: null })
    })
    setMsg('')
  }

  if (!token) return <AuthForm onAuth={authenticate} />

  return (
    <div className="chat">
      <header>
        <h2>Messenger — {username}</h2>
        <div>
          <span className={connected ? 'on' : 'off'}>
            {connected ? '● connected' : '○ connecting…'}
          </span>
          <button className="logout" onClick={logout}>Logout</button>
        </div>
      </header>

      <div className="messages">
        {messages.length === 0 && <div className="empty">No messages yet</div>}
        {messages.map((m, i) => (
          <div key={i} className={`msg ${m.kind.toLowerCase()}`}>
            <span className="time">{m.time}</span>
            <span className="kind">{m.kind}</span>
            <span className="body">{m.body}</span>
          </div>
        ))}
      </div>

      <div className="compose">
        <input
          placeholder="to (username)"
          value={to}
          onChange={e => setTo(e.target.value)}
        />
        <input
          placeholder="message"
          value={msg}
          onChange={e => setMsg(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && sendPrivate()}
        />
        <button onClick={sendPrivate} disabled={!connected || !msg || !to}>Send</button>
        <button onClick={sendBroadcast} disabled={!connected || !msg}>Broadcast</button>
      </div>
    </div>
  )
}

function AuthForm({ onAuth }) {
  const [mode, setMode] = useState('login')
  const [u, setU] = useState('')
  const [p, setP] = useState('')
  const submit = async () => {
    const result = await onAuth(mode, u, p)
    if (result?.registered) setMode('login')
  }
  return (
    <div className="login">
      <div className="tabs">
        <button
          className={mode === 'login' ? 'tab active' : 'tab'}
          onClick={() => setMode('login')}
        >Login</button>
        <button
          className={mode === 'register' ? 'tab active' : 'tab'}
          onClick={() => setMode('register')}
        >Register</button>
      </div>
      <input
        placeholder="username"
        value={u}
        onChange={e => setU(e.target.value)}
        onKeyDown={e => e.key === 'Enter' && submit()}
      />
      <input
        placeholder="password"
        type="password"
        value={p}
        onChange={e => setP(e.target.value)}
        onKeyDown={e => e.key === 'Enter' && submit()}
      />
      <button onClick={submit} disabled={!u || !p}>
        {mode === 'login' ? 'Login' : 'Create account'}
      </button>
    </div>
  )
}
