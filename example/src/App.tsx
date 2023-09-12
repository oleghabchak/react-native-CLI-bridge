import * as React from 'react';
import { useEffect } from 'react';

import { Button, NativeEventEmitter, StyleSheet, View } from 'react-native';
import { DexDownload, sdmmodule, showDeviceManager, sioModule } from 'rnbridge';

const deviceManagerEmitterSDM = new NativeEventEmitter(sdmmodule);
const deviceManagerEmitteSIO = new NativeEventEmitter(sioModule);

export default function App() {
  useEffect(() => {
    const subscription = deviceManagerEmitterSDM.addListener(
      'sdmEventNewDexData',
      (event) => {
        console.log('sdmEventNewDexData' + JSON.stringify(event));
      }
    );

    return () => {
      subscription.remove();
    };
  }, []);

  useEffect(() => {
    const subscription = deviceManagerEmitteSIO.addListener(
      'sioDexEventNewData',
      (event) => {
        console.log('sioDexEventNewData' + JSON.stringify(event));
      }
    );

    return () => {
      subscription.remove();
    };
  }, []);

  useEffect(() => {
    const subscription = deviceManagerEmitteSIO.addListener(
      'sioDexEventTrace',
      (event) => {
        console.log('sioDexEventTrace' + JSON.stringify(event));
      }
    );

    return () => {
      subscription.remove();
    };
  }, []);

  useEffect(() => {
    console.log('add listener for sdmEventDeviceState');
    const subscription = deviceManagerEmitterSDM.addListener(
      'sdmEventDeviceState',
      (event) => {
        console.log('New device state ' + event.address + ': ' + event.state);
      }
    );

    return () => {
      subscription.remove();
    };
  });

  return (
    <View style={styles.container}>
      <Button
        title="Call showDeviceManager"
        onPress={() => showDeviceManager()}
      />
      <Button
        title="Dex Download"
        // onPress={() => DexDownload('BlueSnap DB9 (7B8294)')}
        onPress={() => DexDownload('C8:48:D3:7B:82:94')}
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