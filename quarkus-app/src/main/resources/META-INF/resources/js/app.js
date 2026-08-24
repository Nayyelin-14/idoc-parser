/* IDoc Parser UI — React 18 + htm (no build step) */
const { useState, useEffect, useRef, useCallback } = React;
const html = htm.bind(React.createElement);

const API = '/api/parser-service';

/* ── JSON syntax highlighting ─────────────────────────── */
function highlightJson(json) {
  const escaped = json
    .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  return escaped.replace(
    /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false)\b|\bnull\b|-?\d+(?:\.\d+)?(?:[eE][+-]?\d+)?)/g,
    (match) => {
      let cls = 'j-num';
      if (/^"/.test(match)) {
        cls = /:$/.test(match) ? 'j-key' : 'j-str';
      } else if (/true|false/.test(match)) {
        cls = 'j-bool';
      } else if (/null/.test(match)) {
        cls = 'j-null';
      }
      return `<span class="${cls}">${match}</span>`;
    }
  );
}

/* ── Health pill ──────────────────────────────────────── */
function HealthPill() {
  const [health, setHealth] = useState(null);

  const check = useCallback(async () => {
    try {
      const res = await fetch(`${API}/health`);
      if (!res.ok) throw new Error('unhealthy');
      const data = await res.json();
      setHealth(data.status === 'UP' ? data : null);
    } catch {
      setHealth(null);
    }
  }, []);

  useEffect(() => {
    check();
    const t = setInterval(check, 15000);
    return () => clearInterval(t);
  }, [check]);

  const up = health != null;
  return html`
    <span class=${`health-pill ${up ? 'up' : 'down'}`}>
      <span class="dot"></span>
      ${up ? `${health.service} v${health.version}` : 'service offline'}
    </span>
  `;
}

/* ── Metadata form ────────────────────────────────────── */
function MetadataForm({ fields, values, onChange }) {
  if (!fields || fields.length === 0) return null;
  return html`
    <div class="meta-grid">
      ${fields.map(f => html`
        <div class="meta-field" key=${f.name}>
          <label title=${f.description}>${f.name}</label>
          <input
            type="text"
            value=${values[f.name] || ''}
            placeholder=${f.description}
            onInput=${e => onChange({ ...values, [f.name]: e.target.value })}
          />
        </div>
      `)}
    </div>
  `;
}

/* ── App ──────────────────────────────────────────────── */
function App() {
  const [theme, setTheme] = useState(() => localStorage.getItem('idoc-theme') || 'dark');
  const [input, setInput] = useState('');
  const [meta, setMeta] = useState({});
  const [configFields, setConfigFields] = useState([]);
  const [showMeta, setShowMeta] = useState(false);
  const [busy, setBusy] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const fileRef = useRef(null);

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('idoc-theme', theme);
  }, [theme]);

  /* fetch config schema for metadata labels */
  useEffect(() => {
    fetch(`${API}/config-schema`)
      .then(r => r.json())
      .then(schema => {
        setConfigFields(Object.entries(schema).map(([key, description]) => ({
          name: key.replace('schemaMetadata.', ''),
          description,
        })));
      })
      .catch(() => setConfigFields([]));
  }, []);

  const loadSample = async (name) => {
    try {
      const res = await fetch(`/samples/${name}`);
      const text = await res.text();
      setInput(text);
      setError(null);
    } catch {
      setError(`Could not load sample: ${name}`);
    }
  };

  const onFile = (e) => {
    const file = e.target.files && e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = ev => setInput(String(ev.target.result));
    reader.readAsText(file);
    e.target.value = '';
  };

  const generate = async () => {
    if (!input.trim() || busy) return;
    setBusy(true);
    setError(null);
    setResult(null);
    try {
      const filled = Object.entries(meta).filter(([, v]) => v && v.trim() !== '');
      const body = { input };
      if (filled.length > 0) {
        body.schemaMetadata = Object.fromEntries(filled);
      }
      const res = await fetch(`${API}/execute`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      const data = await res.json();
      if (!res.ok) {
        setError(data.error || `Request failed (${res.status})`);
      } else if (data.success === false) {
        setError(data.output || 'Parsing failed');
      } else {
        setResult(data);
      }
    } catch (e) {
      setError(`Request failed: ${e.message}`);
    } finally {
      setBusy(false);
    }
  };

  const copySchema = () => {
    if (result && result.schema) {
      navigator.clipboard.writeText(JSON.stringify(result.schema, null, 2));
    }
  };

  const metaFilled = Object.values(meta).some(v => v && v.trim() !== '');

  return html`
    <div>
      <header class="app-bar">
        <div class="app-bar-left">
          <div>
            <div class="app-title">IDoc Parser</div>
            <div class="app-subtitle">SAP WE60 IDoc definitions → JSON Schema</div>
          </div>
        </div>
        <div class="app-bar-right">
          <${HealthPill} />
          <button class="icon-btn" title="Toggle theme"
            onClick=${() => setTheme(t => t === 'dark' ? 'light' : 'dark')}>
            ${theme === 'dark' ? '☀' : '☾'}
          </button>
        </div>
      </header>

      <main class="container">
        <!-- ── Input ── -->
        <div class="card">
          <div class="card-header">
            <h2>Input — WE60 IDoc definition</h2>
            <div class="toolbar">
              <button class="btn" onClick=${() => loadSample('ORDERS05-basic.txt')}>Sample: ORDERS05 basic</button>
              <button class="btn" onClick=${() => loadSample('ORDERS05-groups.txt')}>Sample: ORDERS05 groups</button>
              <button class="btn" onClick=${() => fileRef.current.click()}>Upload file…</button>
              <input ref=${fileRef} type="file" accept=".txt" style=${{ display: 'none' }} onChange=${onFile} />
              <button class="btn" onClick=${() => { setInput(''); setResult(null); setError(null); }}>Clear</button>
            </div>
          </div>
          <div class="card-body">
            <textarea
              placeholder={'Paste the SAP WE60 export here…\n\nBEGIN_IDOC ORDERS05\n  BEGIN_SEGMENT E2EDK01005\n    SEGMENTTYPE E1EDK01\n    ...'}
              value=${input}
              onInput=${e => setInput(e.target.value)}
              spellcheck=${false}
            />
          </div>
        </div>

        <!-- ── Metadata (optional) ── -->
        <div class="card">
          <div class="card-header">
            <h2>Schema metadata ${metaFilled ? html`<span class="badge">set</span>` : '(optional)'}</h2>
            <button class="btn" onClick=${() => setShowMeta(v => !v)}>
              ${showMeta ? 'Hide' : 'Show'} fields
            </button>
          </div>
          ${showMeta && html`
            <div class="card-body">
              <${MetadataForm} fields=${configFields} values=${meta} onChange=${setMeta} />
            </div>
          `}
        </div>

        <!-- ── Actions ── -->
        <div class="toolbar" style=${{ marginBottom: '16px' }}>
          <button class="btn btn-primary" disabled=${!input.trim() || busy} onClick=${generate}>
            ${busy ? html`<span class="spinner" />` : '▶'} Generate Schema
          </button>
        </div>

        ${error && html`<div class="banner error">✕ ${error}</div>`}
        ${result && html`
          <div class="banner success">
            ✓ ${result.output || 'Schema generated'}${result.schemaName ? ` — ${result.schemaName}` : ''}
          </div>
        `}

        <!-- ── Result: before / after ── -->
        <div class="split">
          <div class="card">
            <div class="card-header"><h2>Before — IDoc input</h2></div>
            <div class="card-body">
              ${input
                ? html`<div class="code-pane">${input}</div>`
                : html`<div class="empty">No input yet — paste a WE60 export or load a sample.</div>`}
            </div>
          </div>
          <div class="card">
            <div class="card-header">
              <h2>After — JSON Schema</h2>
              ${result && result.schema && html`
                <div class="toolbar">
                  <button class="btn" onClick=${copySchema}>Copy</button>
                </div>
              `}
            </div>
            <div class="card-body">
              ${result && result.schema
                ? html`<div class="code-pane"
                      dangerouslySetInnerHTML=${{ __html: highlightJson(JSON.stringify(result.schema, null, 2)) }} />`
                : html`<div class="empty">Schema will appear here after parsing.</div>`}
            </div>
          </div>
        </div>
      </main>
    </div>
  `;
}

ReactDOM.createRoot(document.getElementById('root')).render(html`<${App} />`);
