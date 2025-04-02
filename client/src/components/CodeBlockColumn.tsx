import React, { useContext, useState, useEffect } from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
// import { atomDark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { vscDarkPlus as atomDark } from 'react-syntax-highlighter/dist/cjs/styles/prism';
import { Checkbox } from '@shivangi_2408/effective-ui';
import { WidgetContext } from '../services/RealTimeUpdates';
import hljs from 'highlight.js';

const CodeBlockColumn: React.FC = () => {
  const [showLineNumbers, setShowLineNumbers] = useState(true);
  const [language, setLanguage] = useState<string>('javascript');
  const { currentWidget } = useContext(WidgetContext);
 
  useEffect(() => {
    if (currentWidget?.code) {
      const detected = hljs.highlightAuto(currentWidget.code);
      setLanguage(detected.language || 'javascript'); // Fallback to javascript if detection fails
    }
  }, [currentWidget]);
 
  return (
<div className="column code-block">
<h2>Code Block ({language})</h2>
      {currentWidget ? (
<>
<SyntaxHighlighter
            language={language}
            style={atomDark}
            showLineNumbers={showLineNumbers}
            wrapLongLines
>
            {currentWidget.code}
</SyntaxHighlighter>
<Checkbox
            color="primary"
            label="Show Line Numbers"
            radius="sm"
            size="sm"
            checked={showLineNumbers}
            onChange={() => setShowLineNumbers(!showLineNumbers)}
          />
</>
      ) : (
<p>No code available yet</p>
      )}
</div>
  );
};
 
export default CodeBlockColumn;