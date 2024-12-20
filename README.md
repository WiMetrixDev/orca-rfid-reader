# Orca RFID Reader

UHF RFID reader module for ORCA-50 handheld device

## Installation

To install run:

```bash
pnpm add orca-rfid-reader
```

or

```bash
npm install orca-rfid-reader
```

or

```bash
yarn add orca-rfid-reader
```

## Usage

### `startReader`

This makes the connection to UHF RFID reader and starts scanning the background. It starts an
observer in the native code and returns a boolean indicating if the connection was successful.

It accepts two arguments:

1. Serial port, which is the serial port to connect to for the UHF RFID reade. The value should be
   one of the values returned by `listSerialPorts`.
2. Baud rate, which is the baud rate to use for the serial connection. The value should be one of
   the values returned by `listBaudRates`.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

const isConnected = OrcaRfidReader.startReader("uhf", "/dev/ttyS4", 115200);
```

### `getReaderPower`

This returns the current power of the UHF RFID reader. This value is normalized to a range of 0-100.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

const power = OrcaRfidReader.getReaderPower();
```

### `setReaderPower`

This sets the power of the UHF RFID reader. The value should be in the range of 0-100.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

OrcaRfidReader.setReaderPower(50);
```

### `addRFIDReadListener`

This adds a listener to the UHF RFID reader. The listener is called whenever a tag is read.

This won't receive any data until the reader is started.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

OrcaRfidReader.addRFIDReadListener(({ rssi, epc }) => {
	console.log(rssi, epc);
});
```

This returns a function that can be called to remove the listener.

### `listSerialPorts`

This returns a list of available serial ports on the device (the options that can be passed to
`startReader`).

```typescript
import OrcaRfidReader from "orca-rfid-reader";

const ports = OrcaRfidReader.listSerialPorts();
```

### `listBaudRates`

This returns a list of available baud rates that can be passed to `startReader`. The available
rates are hardcoded to the following values: `[9600, 19200, 38400, 57600, 115200]`.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

const baudRates = OrcaRfidReader.listBaudRates();
```

### `enableBeep`

This enables the beep sound when a tag is read.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

OrcaRfidReader.enableBeep();
```

### `disableBeep`

This disables the beep sound when a tag is read.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

OrcaRfidReader.disableBeep();
```

### `getBeepStatus`

This returns the current status of the beep sound when a tag is read.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

const isBeepEnabled = OrcaRfidReader.getBeepStatus();
```

### `setMatchEPCs`

This sets the EPCs to match against when reading tags. The EPCs should a
string composed of EPCs separated by commas.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

OrcaRfidReader.setMatchEPCs(
	"E28011606000000000000000,E28011606000000000000001"
);
```

Based on the value of the matching criteria, the reader will beep when the tag is read.

### `resetMatchEPCs`

This resets the EPCs to match against when reading tags.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

OrcaRfidReader.resetMatchEPCs();
```

We want this because when no EPCs are set, the reader will beep when any tag is read.

### `stopReader`

This disconnects the UHF RFID reader and stops scanning. This should be called when the reader is
no longer needed to free up resources and prevent battery drain.

```typescript
import OrcaRfidReader from "orca-rfid-reader";

OrcaRfidReader.stopReader();
```
