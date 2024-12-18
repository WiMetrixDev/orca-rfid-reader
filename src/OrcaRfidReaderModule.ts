import { NativeModule, requireNativeModule } from "expo";

export const BAUD_RATES = [9600, 19200, 38400, 57600, 115200] as const;

export type TBaudRates = typeof BAUD_RATES;
export type TBaudRate = TBaudRates[number];
export type TSerialPort = `/dev/tty${1 | 2 | 3 | 4}` | (string & {});
export type RFIDReadEventPayload = {
	epc: string;
	rssi: string;
};
export type OrcaRfidReaderEvents = {
	onRFIDRead: (params: RFIDReadEventPayload) => void;
};

declare class OrcaRfidReader extends NativeModule<OrcaRfidReaderEvents> {
	startReader(serialPort: TSerialPort, baudRate: TBaudRate): boolean;
	getReaderPower(): number;
	setReaderPower(power: number): void;
	listSerialPorts(): TSerialPort[];
	listBaudRates(): TBaudRates;
	setPower(power: number): void;
	enableBeep(): void;
	disableBeep(): void;
	getBeepStatus(): boolean;
	setMatchEPCs(newMatchEPCs: string): void;
	resetMatchEPCs(): void;
	stopReader(): void;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<OrcaRfidReader>("OrcaRfidReader");
