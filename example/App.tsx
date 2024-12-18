import { useEffect, useState } from "react";
import {
	Alert,
	Button,
	FlatList,
	StatusBar,
	StyleSheet,
	Text,
	View,
} from "react-native";

import OrcaRfidReader from "orca-rfid-reader";

type Reading = {
	epc: string;
	rssi: string;
};

export default function App() {
	const [connected, setConnected] = useState(false);
	const [readings, setReadings] = useState<Reading[]>([]);

	const connect = () => {
		const connected = OrcaRfidReader.startReader("/dev/ttyS4", 115200);

		if (!connected) {
			console.error("Failed to connect to UHF reader");
			Alert.alert("Failed to connect to UHF reader");
			setConnected(false);
			return;
		}

		setConnected(true);

		console.log("Connected to UHF reader");
	};

	useEffect(() => {
		connect();

		const subscription = OrcaRfidReader.addListener(
			"onRFIDRead",
			({ epc, rssi }) => {
				console.log("Read EPC:", epc, "RSSI:", rssi);
				setReadings((prev) => [...prev, { epc, rssi }]);
			}
		);

		return () => {
			subscription.remove();
		};
	}, []);

	return (
		<View style={styles.container}>
			<Text
				style={{
					fontSize: 36,
					fontWeight: "bold",
				}}
			>
				Orca RFID Reader
			</Text>
			<Button
				title="Connect"
				onPress={() => {
					connect();
				}}
				disabled={connected}
				color={"green"}
			/>
			<Button
				title="Disconnect"
				onPress={() => {
					OrcaRfidReader.stopReader();
					setConnected(false);
				}}
				disabled={!connected}
				color={"red"}
			/>
			<Button
				title="Clear Readings"
				onPress={() => {
					setReadings([]);
				}}
			/>
			<StatusBar
				animated={true}
				backgroundColor={connected ? "green" : "red"}
				barStyle="light-content"
			/>
			<Text
				style={{
					fontSize: 24,
					fontWeight: "bold",
				}}
			>
				Readings:
			</Text>
			<FlatList
				data={readings}
				renderItem={({ item, index }) => (
					<View
						style={{
							backgroundColor: index % 2 === 0 ? "lightgray" : "white",
							padding: 10,
							flexDirection: "row",
							gap: 10,
						}}
					>
						<Text>EPC: {item.epc}</Text>
						<Text>RSSI: {item.rssi}</Text>
					</View>
				)}
				contentContainerStyle={{
					flexGrow: 1,
					gap: 10,
				}}
			/>
		</View>
	);
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		backgroundColor: "#fff",
		// alignItems: "center",
		justifyContent: "center",
		gap: 10,
		padding: 20,
	},
});
