import * as React from 'react';
import { useEffect } from 'react';

import { Button, StyleSheet, Text, View } from 'react-native';
import {
  showDeviceManager,
  sdmmodule,
  sendCommand,
  OnSendCommand,
  DexDownload,
  OnDexDownload,
} from 'react-native-awesome-module2';

// const deviceManagerEmitter = new NativeEventEmitter(sdmmodule);

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  useEffect(() => {
    OnSendCommand((e: any) => {
      console.log('OnSendCommand ', e);
    });

    OnDexDownload((e: any) => {
      console.log('OnDexDownload ', e);
    });
  }, []);

  // useEffect(() => {
  //   const subscription = deviceManagerEmitter.addListener('sdmEventNewData', event => {
  //     console.log('new data from ' + event.address + ': ' + event.data);
  //   });

  //   return () => {
  //     subscription.remove();
  //   }
  // }, []);

  // useEffect(() => {
  //   console.log('add listener for sdmEventDeviceState');
  //   const subscription = deviceManagerEmitter.addListener('sdmEventDeviceState', event => {
  //     console.log('New device state ' + event.address + ': ' + event.state);
  //   });

  //   return () => {
  //     subscription.remove();
  //   }
  // });

  const handlesend = () => {
    console.log('1 ===');
    sendCommand('BlueSnap DB9 (7B8294)', [0x50, 0x0a, 0x0d]);
    console.log('2 ===');
  };

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Button
        title="Call showDeviceManager"
        onPress={() => showDeviceManager()}
      />
      <Button 
        title="send Command" 
        onPress={() => handlesend()} 
        />
      <Button
        title="Dex Download"
        onPress={() => DexDownload('BlueSnap DB9 (7B8294)')}
      />
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    height: 'auto',
    justifyContent: 'space-around',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});