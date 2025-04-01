import React, { useState } from "react";
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { atomDark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { Checkbox } from '@shivangi_2408/effective-ui';

const Code = `function createStyleObject(classNames, style) {
  return classNames.reduce((styleObject, className) => {
    return {...styleObject, ...style[className]};
  }, {});
}`;

const CodeBlockColumn: React.FC = () => {
  const [showLineNumbers, setShowLineNumbers] = useState(true);

  return (
    <div className="column code-block">
      <h2>Code Block</h2>
      <SyntaxHighlighter
        language="javascript"
        style={atomDark}
        showLineNumbers={showLineNumbers}
        wrapLongLines
      >
        {Code}
      </SyntaxHighlighter>
      <Checkbox
        color="primary"
        label="Show Line Numbers"
        radius="sm"
        size="sm"
        checked={showLineNumbers}
        onChange={() => setShowLineNumbers(!showLineNumbers)}
      />
    </div>
  );
};

export default CodeBlockColumn;