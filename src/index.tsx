import { NativeModules, Platform } from 'react-native';

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
export function showDeviceManager() {
  return SDMModule.showDeviceManager();
}

export function sendCommand(addres, command) {
  return NativeModules.SDMModule.sendCommand(addres, command);
}

export const sdmmodule = NativeModules.SDMModule;
