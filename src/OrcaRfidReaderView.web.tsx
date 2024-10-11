import * as React from 'react';

import { OrcaRfidReaderViewProps } from './OrcaRfidReader.types';

export default function OrcaRfidReaderView(props: OrcaRfidReaderViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
