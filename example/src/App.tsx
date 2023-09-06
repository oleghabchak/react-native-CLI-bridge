import * as React from 'react';
import { StyleSheet, View, Button } from 'react-native';
import { showDeviceManager } from 'react-native-awesome-module2';
export default function App() {
  return (
    <View style={styles.container}>
      <Button 
        title="CAll showDeviceManager@@@"
        onPress={() => showDeviceManager()} 
        />
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});