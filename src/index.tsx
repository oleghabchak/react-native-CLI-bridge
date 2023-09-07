import { NativeModules, NativeEventEmitter, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-awesome-module2' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

  const SDMModule = NativeModules.AwesomeModule2
  ? NativeModules.AwesomeModule2
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

    function setEmitter(name: string, fn: (e: any) => void) {
      const eventEmitter = new NativeEventEmitter();
      SDMModule.eventListener = eventEmitter.addListener(name, (event) => {
        fn(event);
      });
    }
export function showDeviceManager() {
  return SDMModule.showDeviceManager();
}

export function sendCommand(addres: string, command: number[]) {
  return SDMModule.sendCommand(addres, command);
}
export function OnSendCommand(fn: (e: any) => void) {
  setEmitter('sendCommand', fn);
}

export const sdmmodule = NativeModules.SDMModule;