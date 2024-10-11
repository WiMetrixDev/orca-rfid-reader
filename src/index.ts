/* eslint-disable @typescript-eslint/no-unsafe-argument */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */

/* eslint-disable @typescript-eslint/no-unsafe-return */

import type { Subscription } from "expo-modules-core";
import { EventEmitter, NativeModulesProxy } from "expo-modules-core";

import OrcaRfidReaderModule from "./OrcaRfidReaderModule";

export const BAUD_RATES = [9600, 19200, 38400, 57600, 115200] as const;

export type TBaudRates = typeof BAUD_RATES;
export type TBaudRate = TBaudRates[number];
// eslint-disable-next-line @typescript-eslint/ban-types
export type TSerialPort = `/dev/tty${1 | 2 | 3 | 4}` | (string & {});

export function startReader(
  serialPort: TSerialPort,
  baudRate: TBaudRate
): boolean {
  return OrcaRfidReaderModule.startReader(serialPort, baudRate);
}

export function getReaderPower(): number {
  return OrcaRfidReaderModule.getReaderPower();
}

export function setReaderPower(power: number): void {
  OrcaRfidReaderModule.setReaderPower(power);
}

export function listSerialPorts(): TSerialPort[] {
  return OrcaRfidReaderModule.listSerialPorts();
}

export function listBaudRates(): TBaudRates {
  return OrcaRfidReaderModule.listBaudRates();
}

export function stopReader(): void {
  OrcaRfidReaderModule.stopReader();
}

const emitter = new EventEmitter(
  OrcaRfidReaderModule ?? NativeModulesProxy.OrcaRFIDReader
);

export type RFIDReadEventPayload = {
  epc: string;
  rssi: string;
};

export function addRFIDReadListener(
  listener: (event: RFIDReadEventPayload) => void
): Subscription {
  return emitter.addListener<RFIDReadEventPayload>("onRFIDRead", listener);
}
