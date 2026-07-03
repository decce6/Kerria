# Changelog

## 1.3.2

Fixed "Resource Reload failed" errors.

## 1.3.1

- [1.19.2-fabric] Fixed crash when opening Video Settings screen

## 1.3.0

- Fixed performance degradation with XyCraft mod
- Added Fast Light Texture Upload optimization
- Backported to 1.19.2, 1.18.2 and 1.16.5

## 1.2.2

This hotfix version temporarily reverts an optimization in 1.2.1 that caused crashes and various other issues.

## 1.2.1

- Fixed crash on startup on certain GPUs
- Improved modded performance by unconditionally applying fast texture upload optimization

## 1.2.0

- Fixed memory usage issues introduced in 1.1.0
- Improved memory footprint: cache is now created on a per-image rather than per-frame basis
- Significantly improved image cache performance (eliminated hash table lookup)
- Fixed a minor GPU memory leak due to pixel buffer objects not released

## 1.1.0

- Reduced the memory footprint of cached images
- Removed "Min Cache Size" and "Max Cache Size" option
- Greatly improved image cache performance in large modpacks
- Removed Caffeine Cache dependency
- Fixed "Fast Texture Upload" and "Animated Texture Cache" in-game option not being respected

## 1.0.7

Fixed embeddium tainted warning on 1.20.1 Forge.

## 1.0.6

Fixed compatibility with Sodium 0.8+.

## 1.0.5

Improved GL capacity detection, now with support for ARB extensions.

## 1.0.4

Fixed crash at startup when OpenGL 4.5 is not supported.

## 1.0.3

Hotfix for an incompatibility with certain shader packs.

## 1.0.2

Fixed a crash at startup happening when used with some mods.

## 1.0.1

- Added GL version check
- Fixed Forge builds not working

## 1.0.0

Initial release.
