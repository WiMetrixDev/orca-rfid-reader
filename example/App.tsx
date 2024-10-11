import { StyleSheet, Text, View } from 'react-native';

import * as OrcaRfidReader from 'orca-rfid-reader';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{OrcaRfidReader.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
