
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNSDMModuleSpec.h"

@interface SDMModule : NSObject <NativeSDMModuleSpec>
#else
#import <React/RCTBridgeModule.h>

@interface SDMModule : NSObject <RCTBridgeModule>
#endif

@end
