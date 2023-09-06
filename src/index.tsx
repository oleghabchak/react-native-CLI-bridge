import { NativeModules, Platform } from 'react-native';

module.exports = NativeModules.SDMModule;

export function showDeviceManager() {
  return NativeModules.SDMModule.showDeviceManager();
}

export function sendCommand(addres, command) {
  return NativeModules.SDMModule.sendCommand(addres, command);
}

export const sdmmodule = NativeModules.SDMModule;
