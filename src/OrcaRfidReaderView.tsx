import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { OrcaRfidReaderViewProps } from './OrcaRfidReader.types';

const NativeView: React.ComponentType<OrcaRfidReaderViewProps> =
  requireNativeViewManager('OrcaRfidReader');

export default function OrcaRfidReaderView(props: OrcaRfidReaderViewProps) {
  return <NativeView {...props} />;
}
