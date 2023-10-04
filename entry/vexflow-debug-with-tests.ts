// Copyright (c) 2023-present VexFlow contributors: https://github.com/vexflow/vexflow/graphs/contributors
// MIT License
//
// vexflow-debug-with-tests.ts is the entry point for the build output file vexflow-debug-with-tests.js.
// It includes the tests from vexflow/tests/.
// The output file is used by flow.html & flow-headless-browser.html to run the tests.

import * as VexSrc from '../src/index';
import * as VexTests from '../tests/index';

import { Flow } from '../src/flow';

Flow.setMusicFont('Bravura', 'Roboto Slab');

// Re-export all exports from src/index.ts and tests/index.ts.
export * from '../src/index';
export * from '../tests/index';
// Also collect all exports into a default export for CJS projects.
export default {
  ...VexSrc,
  ...VexTests,
};
