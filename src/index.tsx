import { NativeModules, Platform } from 'react-native';
module.exports = NativeModules.SDMModule;

const LINKING_ERROR =
  `The package 'react-native-awesome-module2' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

  const SDMModule = NativeModules.SDMModule
  ? NativeModules.SDMModule
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