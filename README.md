# Orca RFID Reader

UHF RFID reader module for ORCA-50 handheld device

## Installation

To install just copy the `orca-rfid-reader` folder to `modules` directory of your Expo project.
You will need to rebuild the project after adding the module.

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
import { startReader } from "orca-rfid-reader";

const isConnected = await startReader("uhf", "/dev/ttyS4", 115200);
```

### `getReaderPower`

This returns the current power of the UHF RFID reader. This value is normalized to a range of 0-100.

```typescript
import { getReaderPower } from "orca-rfid-reader";

const power = getReaderPower();
```

### `setReaderPower`

This sets the power of the UHF RFID reader. The value should be in the range of 0-100.

```typescript
import { setReaderPower } from "orca-rfid-reader";

setReaderPower(50);
```

### `addRFIDReadListener`

This adds a listener to the UHF RFID reader. The listener is called whenever a tag is read.

This won't receive any data until the reader is started.

```typescript
import { addRFIDReadListener } from "orca-rfid-reader";

addRFIDReadListener(({ rssi, epc }) => {
	console.log(rssi, epc);
});
```

This returns a function that can be called to remove the listener.

### `listSerialPorts`

This returns a list of available serial ports on the device (the options that can be passed to
`startReader`).

```typescript
import { listSerialPorts } from "orca-rfid-reader";

const ports = await listSerialPorts();
```

### `listBaudRates`

This returns a list of available baud rates that can be passed to `startReader`. The available
rates are hardcoded to the following values: `[9600, 19200, 38400, 57600, 115200]`.

```typescript
import { listBaudRates } from "orca-rfid-reader";

const baudRates = listBaudRates();
```

### `stopReader`

This disconnects the UHF RFID reader and stops scanning. This should be called when the reader is
no longer needed to free up resources and prevent battery drain.

```typescript
import { stopReader } from "orca-rfid-reader";

stopReader();
```
