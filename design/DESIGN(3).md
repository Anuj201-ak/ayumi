---
name: Ayumi
colors:
  surface: '#fff8f4'
  surface-dim: '#e4d8cd'
  surface-bright: '#fff8f4'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#fef2e6'
  surface-container: '#f8ece1'
  surface-container-high: '#f2e6db'
  surface-container-highest: '#ece0d5'
  on-surface: '#201b14'
  on-surface-variant: '#44483d'
  inverse-surface: '#362f28'
  inverse-on-surface: '#fbefe3'
  outline: '#75796c'
  outline-variant: '#c4c8ba'
  surface-tint: '#4d6633'
  primary: '#445c2b'
  on-primary: '#ffffff'
  primary-container: '#5c7541'
  on-primary-container: '#ddfbba'
  inverse-primary: '#b3d093'
  secondary: '#a63b01'
  on-secondary: '#ffffff'
  secondary-container: '#fe7a42'
  on-secondary-container: '#652000'
  tertiary: '#425d25'
  on-tertiary: '#ffffff'
  tertiary-container: '#5a763b'
  on-tertiary-container: '#dbfcb3'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#cfecad'
  primary-fixed-dim: '#b3d093'
  on-primary-fixed: '#0f2000'
  on-primary-fixed-variant: '#364d1e'
  secondary-fixed: '#ffdbce'
  secondary-fixed-dim: '#ffb599'
  on-secondary-fixed: '#370e00'
  on-secondary-fixed-variant: '#7f2b00'
  tertiary-fixed: '#cceda6'
  tertiary-fixed-dim: '#b1d18c'
  on-tertiary-fixed: '#0f2000'
  on-tertiary-fixed-variant: '#344e18'
  background: '#fff8f4'
  on-background: '#201b14'
  surface-variant: '#ece0d5'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '600'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 30px
    fontWeight: '600'
    lineHeight: 38px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-md:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
  label-sm:
    fontFamily: Manrope
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-sm: 0.75rem
  gutter-lg: 1.5rem
  margin: 1.25rem
  margin-sm: 1rem
  margin-lg: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2.25rem
---

## Brand & Style

This design system embodies an elevated Zen minimalist philosophy designed to instill quiet clarity, steady focus, and unhurried progression. Drawing inspiration from tactile papercraft (*washi*), artisanal ceramics, and natural Japanese tea rituals, the interface avoids digital urgency and notification stress.

The target audience consists of mindful individuals seeking deliberate daily routines, contemplative time-tracking, and quiet personal reflection without game-like friction or overwhelming sensory overload. 

Key style tenets:
- **Quiet Clarity:** Ample negative space, unforced visual balance, and low-cognitive-load layouts.
- **Organic Tactility:** Subtle paper-like surface shifts layered over serene cream bases rather than harsh contrasting lines.
- **Mindful Progression:** Interaction moments feel deliberate and grounded, replacing abrupt snaps with soft, fluid transitions.

## Colors

The palette is anchored by ceremonial matcha green and organic washi paper tones, highlighted with warm persimmon for focus and intent.

- **Primary (`#5C7541`):** Deep grounding matcha green. Used for current states ("Now"), high-priority confirmations, progress milestones, and active navigational signals.
- **Secondary (`#E86A33`):** Warm persimmon orange. Acts as a spark of human energy, reserved for intentional call-to-actions, active streaks, and primary task completion.
- **Tertiary (`#7C9A5B`):** Leaf matcha. Serves as a soothing secondary accent for tags, supportive badges, and delicate progress indicators.
- **Neutral (`#2A241D`):** Deep sumi ink. Provides legible, humanized text without the clinical harshness of true pitch black.

### Surface Tones & Backgrounds
- **Base Canvas:** Warm washi cream (`#F6F1E7` to `#FAF6EE`) to establish a textured, non-glare foundation.
- **Cards & Elevated Containers:** Crisp warm white (`#FFFFFF` to `#FDFBF7`) creating distinct tonal hierarchy without stark borders.
- **Ink Tiers:** Primary ink (`#2A241D`), secondary ink (`#6B6155`), and muted border ink (`#E3DBD0`).

## Typography

The typography pairs the open geometry of **Plus Jakarta Sans** for headers with the clean clarity of **Manrope** for body and data layers. 

- **Display & Headlines:** Headings use medium to semibold weights with subtle negative tracking, creating an inviting, human presence that avoids geometric rigidity.
- **Numbers & Metrics:** Routine tracking numerals rely on large, tabular or semi-expanded figures that convey stability and calm progression.
- **Body & Captions:** Optimized for sustained reading on mobile displays with generous line heights to preserve breathing room between lines.

## Layout & Spacing

The layout adheres to an uncluttered, single-column fluid mobile flow inspired by Material 3, adapting across form factors with an emphasis on natural vertical breathing space.

- **Mobile (Default portrait):** Uses a fluid column system with `1.25rem` screen margins and `0.75rem` to `1rem` intra-card gutters. The vertical rhythm is spacious, using `space-lg` and `space-xl` to segment distinct routine blocks (e.g., Morning, Midday, Evening).
- **Tablet / Expanded:** Retains the centered calm view by constraining core routine lists to a maximum width of `680px`, preventing tracking elements from stretching excessively.
- **Rhythm Philosophy:** Spacing operates on a multiple of 4dp/8dp. Elements within a routine card cluster closely (`space-xs` and `space-sm`), while sections maintain wide separations to prevent visual fatigue.

## Elevation & Depth

Visual hierarchy uses tonal surface stacking complemented by ultra-soft, diffused ambient shadows rather than stark elevation jumps.

- **Canvas Foundation:** Layer 0 sits at the base washi tone (`#F6F1E7`).
- **Surface Elevation (Cards & Containers):** Layer 1 sits on `#FFFFFF` or `#FDFBF7` with an ambient drop shadow tinted with warm tea-bark tones: `0 4px 16px rgba(42, 36, 29, 0.04), 0 1px 2px rgba(42, 36, 29, 0.02)`.
- **Active / "Now" Focus:** Layer 2 cards representing current tasks or focused states elevate gently with an ambient matcha-tinted glow: `0 8px 24px rgba(92, 117, 65, 0.08)`.
- **Outlines:** Structural boundaries avoid harsh borders, utilizing low-contrast ink edges (`#E3DBD0` at 1px) or seamless tonal contrasts.

## Shapes

The design uses balanced, rounded geometries (Level 2) that feel organic and comfortable to the touch.

- **Cards & Panels:** Default to `1.125rem` (18dp / `rounded-2xl`) corner radius, creating soft, pebble-like surfaces that evoke polished ceramic and smooth stones.
- **Pills & Chips:** Utilize full continuous radii (`9999px`) for time badges, tag indicators, and quick status pills.
- **Interactive Controls:** Buttons, inputs, and progress sheets feature unified `0.75rem` to `1rem` corners, reinforcing tactile approachability.

## Components

### Buttons
- **Primary:** Filled in deep matcha (`#5C7541`) with warm white text, or persimmon orange (`#E86A33`) for high-conviction actions. Height of 48dp, rounded with `0.75rem` corners.
- **Secondary / Ghost:** Washed background tint (`#EFF5E9` for matcha or `#FDF0E9` for persimmon) with matching colored text. Zero harsh borders.

### Cards & Routine Tiles
- **Routine Item:** Crisp warm white card (`#FFFFFF`) framed by a subtle 1px border (`#E3DBD0`). Content features semibold task titles in sumi ink (`#2A241D`) and small, tranquil subtext in secondary ink (`#6B6155`).
- **"Now" Active Routine Card:** Accentuated with a left-edge 4dp matcha pill indicator, pale matcha tinted wash (`#EFF5E9`), and an ambient matcha elevation.

### Checkboxes & Progress Rings
- **Checkboxes:** Custom circular checks (24dp) with a smooth transition from an open muted border (`#E3DBD0`) to a full matcha or persimmon fill with a crisp checkmark.
- **Daily Rings:** Gentle, low-stroke circular meters with uncompleted arcs in `#EFE8DA` and active arcs in `#5C7541` or `#E86A33`.

### Chips & Tags
- Compact tags with pill-radius (`rounded-full`), padded with `0.25rem 0.75rem`. Tonal backgrounds with dark sumi or matcha typography for time anchors (e.g., "7:30 AM", "Mindfulness").

### Input Fields
- Filled style with subtle warm tint (`#FAF6EE`), transitioning on focus to a crisp white fill with a 1.5px `#5C7541` stroke and no harsh drop shadows.