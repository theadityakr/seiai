import React, { useContext, useState } from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { atomDark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { Checkbox } from '@shivangi_2408/effective-ui';
import { WidgetContext } from '../services/RealTimeUpdates';

const CodeBlockColumn: React.FC = () => {
  const [showLineNumbers, setShowLineNumbers] = useState(true);
  const { currentWidget } = useContext(WidgetContext);

  return (
    <div className="column code-block">
      <h2>Code Block</h2>
      {currentWidget ? (
        <>
          <SyntaxHighlighter
            language="javascript"
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