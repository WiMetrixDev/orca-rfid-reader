import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to OrcaRfidReader.web.ts
// and on native platforms to OrcaRfidReader.ts
import OrcaRfidReaderModule from './OrcaRfidReaderModule';
import OrcaRfidReaderView from './OrcaRfidReaderView';
import { ChangeEventPayload, OrcaRfidReaderViewProps } from './OrcaRfidReader.types';

// Get the native constant value.
export const PI = OrcaRfidReaderModule.PI;

export function hello(): string {
  return OrcaRfidReaderModule.hello();
}

export async function setValueAsync(value: string) {
  return await OrcaRfidReaderModule.setValueAsync(value);
}

const emitter = new EventEmitter(OrcaRfidReaderModule ?? NativeModulesProxy.OrcaRfidReader);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { OrcaRfidReaderView, OrcaRfidReaderViewProps, ChangeEventPayload };
