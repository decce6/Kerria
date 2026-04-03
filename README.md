# Kerria

## Overview

Kerria is a mod that optimizes texture animations.

In vanilla Minecraft, texture animations are handled rather inefficiently. Each time a texture is updated, it is uploaded onto the atlas via the slow `glTexSubImage2D`, which involves CPU->GPU upload and may potentially incur a pipeline stall.

This mod adds two optimizations to reduce the performance impact of animated textures:

- **Texture Animation Cache**. Instead of performing an upload each time, we can cache non-interpolated animated textures once they are uploaded to the GPU. When the texture is subsequently needed, it is applied with a very fast GPU-to-GPU copy.
- **Fast Texture Upload**. Optimize texture upload performance with persistently mapped buffers and pixel buffer objects.

The optimizations can be configured through the Sodium/Embeddium video settings screen.

## FAQs

**Is this compatible with Sodium/Embeddium's "Animate Only Visible Textures" option?**

Yes. That option improves performance by skipping uploading animated textures that are invisible, while this mod actually speeds up the process of uploading animated textures.