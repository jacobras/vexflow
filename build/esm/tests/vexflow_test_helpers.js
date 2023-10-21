var __classPrivateFieldGet = (this && this.__classPrivateFieldGet) || function (receiver, state, kind, f) {
    if (kind === "a" && !f) throw new TypeError("Private accessor was defined without a getter");
    if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver)) throw new TypeError("Cannot read private member from an object whose class did not declare it");
    return kind === "m" ? f : kind === "a" ? f.call(receiver) : f ? f.value : state.get(receiver);
};
var __classPrivateFieldSet = (this && this.__classPrivateFieldSet) || function (receiver, state, value, kind, f) {
    if (kind === "m") throw new TypeError("Private method is not writable");
    if (kind === "a" && !f) throw new TypeError("Private accessor was defined without a setter");
    if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver)) throw new TypeError("Cannot write private member to an object whose class did not declare it");
    return (kind === "a" ? f.call(receiver, value) : f ? f.value = value : state.set(receiver, value)), value;
};
var _a, _VexFlowTests_NEXT_TEST_ID;
import { Factory, Flow, Renderer } from '../src/index.js';
import { Metrics } from '../src/metrics.js';
import { globalObject } from '../src/util.js';
const global = globalObject();
let originalFontNames;
function useTempFontStack(fontName) {
    originalFontNames = Flow.getFonts();
    Flow.setFonts(...VexFlowTests.FONT_STACKS[fontName]);
}
function restoreOriginalFontStack() {
    Flow.setFonts(...originalFontNames);
}
if (!global.$) {
    global.$ = (param) => {
        let element;
        if (typeof param !== 'string') {
            element = param;
        }
        else if (param.startsWith('<')) {
            const tagName = param.match(/[A-Za-z]+/g)[0];
            element = document.createElement(tagName);
        }
        else {
            element = document.querySelector(param);
        }
        const $element = {
            get(index) {
                return element;
            },
            addClass(c) {
                element.classList.add(c);
                return $element;
            },
            text(t) {
                element.textContent = t;
                return $element;
            },
            html(h) {
                if (!h) {
                    return element.innerHTML;
                }
                else {
                    element.innerHTML = h;
                    return $element;
                }
            },
            append(...elementsToAppend) {
                elementsToAppend.forEach((e) => {
                    element.appendChild(e);
                });
                return $element;
            },
            attr(attrName, val) {
                element.setAttribute(attrName, val);
                return $element;
            },
        };
        return $element;
    };
}
function sanitize(text) {
    return text.replace(/[^a-zA-Z0-9]/g, '_');
}
const CANVAS_TEST_CONFIG = {
    backend: Renderer.Backends.CANVAS,
    tagName: 'canvas',
    testType: 'Canvas',
    fontStacks: ['Bravura'],
};
const CANVAS_TEXT_CONFIG = {
    backend: Renderer.Backends.CANVAS,
    tagName: 'canvas',
    testType: 'Canvas',
    fontStacks: ['Bravura'],
};
const SVG_TEST_CONFIG = {
    backend: Renderer.Backends.SVG,
    tagName: 'div',
    testType: 'SVG',
    fontStacks: [
        'Bravura',
        'Finale Ash',
        'Finale Broadway',
        'Finale Maestro',
        'Gonville',
        'Gootville',
        'Leland',
        'MuseJazz',
        'Petaluma',
    ],
};
const SVG_TEXT_CONFIG = {
    backend: Renderer.Backends.SVG,
    tagName: 'div',
    testType: 'SVG',
    fontStacks: ['Bravura'],
};
const NODE_TEST_CONFIG = {
    backend: Renderer.Backends.CANVAS,
    tagName: 'canvas',
    testType: 'NodeCanvas',
    fontStacks: ['Bravura', 'Gonville', 'Petaluma', 'Leland'],
};
export class VexFlowTests {
    static register(test) {
        _a.tests.push(test);
    }
    static parseJobOptions(runOptions) {
        let { jobs, job } = runOptions || { jobs: 1, job: 0 };
        if (window) {
            const { location } = window;
            if (location) {
                const sps = new URLSearchParams(location.search);
                const jobsParam = sps.get('jobs');
                const jobParam = sps.get('job');
                if (jobsParam) {
                    jobs = parseInt(jobsParam, 10);
                }
                if (jobParam) {
                    job = parseInt(jobParam, 10);
                }
            }
        }
        return {
            jobs,
            job,
        };
    }
    static run(runOptions) {
        const { jobs, job } = _a.parseJobOptions(runOptions);
        _a.tests.forEach((test, idx) => {
            if (jobs === 1 || idx % jobs === job) {
                test.Start();
            }
        });
    }
    static set NODE_FONT_STACKS(fontStacks) {
        NODE_TEST_CONFIG.fontStacks = fontStacks;
    }
    static generateTestID(prefix) {
        var _b, _c, _d;
        return prefix + '_' + (__classPrivateFieldSet(_b = _a, _a, (_d = __classPrivateFieldGet(_b, _a, "f", _VexFlowTests_NEXT_TEST_ID), _c = _d++, _d), "f", _VexFlowTests_NEXT_TEST_ID), _c);
    }
    static runTests(name, testFunc, params) {
        _a.runCanvasTest(name, testFunc, params);
        _a.runSVGTest(name, testFunc, params);
        _a.runNodeTest(name, testFunc, params);
    }
    static runTextTests(name, testFunc, params) {
        _a.runCanvasText(name, testFunc, params);
        _a.runSVGText(name, testFunc, params);
    }
    static createTest(elementId, testTitle, tagName, titleId = '') {
        const anchorTestTitle = `<a href="#${titleId}">${testTitle}</a>`;
        const title = $('<div/>').addClass('name').attr('id', titleId).html(anchorTestTitle).get(0);
        const vexOutput = $(`<${tagName}/>`).addClass('vex-tabdiv').attr('id', elementId).get(0);
        const container = $('<div/>').addClass('testcanvas').append(title, vexOutput).get(0);
        $('#qunit-tests').append(container);
        return vexOutput;
    }
    static makeFactory(options, width = 450, height = 140) {
        const { elementId, backend } = options;
        return new Factory({ renderer: { elementId, backend, width, height } });
    }
    static runCanvasTest(name, testFunc, params) {
        if (_a.RUN_CANVAS_TESTS) {
            const helper = null;
            _a.runWithParams(Object.assign(Object.assign({}, CANVAS_TEST_CONFIG), { name, testFunc, params, helper }));
        }
    }
    static runCanvasText(name, testFunc, params) {
        if (_a.RUN_CANVAS_TESTS) {
            const helper = null;
            _a.runWithParams(Object.assign(Object.assign({}, CANVAS_TEXT_CONFIG), { name, testFunc, params, helper }));
        }
    }
    static runSVGTest(name, testFunc, params) {
        if (_a.RUN_SVG_TESTS) {
            const helper = null;
            _a.runWithParams(Object.assign(Object.assign({}, SVG_TEST_CONFIG), { name, testFunc, params, helper }));
        }
    }
    static runSVGText(name, testFunc, params) {
        if (_a.RUN_SVG_TESTS) {
            const helper = null;
            _a.runWithParams(Object.assign(Object.assign({}, SVG_TEXT_CONFIG), { name, testFunc, params, helper }));
        }
    }
    static runNodeTest(name, testFunc, params) {
        if (_a.RUN_NODE_TESTS) {
            const helper = _a.runNodeTestHelper;
            _a.runWithParams(Object.assign(Object.assign({}, NODE_TEST_CONFIG), { name, testFunc, params, helper }));
        }
    }
    static runNodeTestHelper(fontName, element) {
        if (Renderer.lastContext !== undefined) {
            const fileName = _a.NODE_IMAGEDIR +
                '/' +
                sanitize(QUnit.moduleName) +
                '.' +
                sanitize(QUnit.testName) +
                '.' +
                sanitize(fontName) +
                '.jsdom.png';
            const imageData = element.toDataURL().split(';base64,').pop();
            const imageBuffer = Buffer.from(imageData, 'base64');
            _a.shims.fs.writeFileSync(fileName, imageBuffer, { encoding: 'base64' });
        }
    }
    static runWithParams({ fontStacks, testFunc, name, params, backend, tagName, testType, helper }) {
        if (name === undefined) {
            throw new Error('Test name is undefined.');
        }
        const testTypeLowerCase = testType.toLowerCase();
        fontStacks.forEach((fontStackName) => {
            QUnit.test(name, (assert) => {
                useTempFontStack(fontStackName);
                const sanitizedFontStackName = sanitize(fontStackName);
                const elementId = _a.generateTestID(`${testTypeLowerCase}_` + sanitizedFontStackName);
                const moduleName = assert.test.module.name;
                const title = moduleName + ' › ' + name + ` › ${testType} + ${fontStackName}`;
                const prefix = testTypeLowerCase + '_';
                const titleId = `${prefix}${sanitize(moduleName)}.${sanitize(name)}.${sanitizedFontStackName}`;
                const element = _a.createTest(elementId, title, tagName, titleId);
                const options = { elementId, params, assert, backend };
                const isSVG = backend === Renderer.Backends.SVG;
                const contextBuilder = isSVG ? Renderer.getSVGContext : Renderer.getCanvasContext;
                testFunc(options, contextBuilder);
                restoreOriginalFontStack();
                if (helper)
                    helper(fontStackName, element);
            });
        });
    }
    static plotLegendForNoteWidth(ctx, x, y) {
        ctx.save();
        ctx.setFont(Metrics.get('fontFamily'), 8);
        const spacing = 12;
        let lastY = y;
        function legend(color, text) {
            ctx.beginPath();
            ctx.setStrokeStyle(color);
            ctx.setFillStyle(color);
            ctx.setLineWidth(10);
            ctx.moveTo(x, lastY - 4);
            ctx.lineTo(x + 10, lastY - 4);
            ctx.stroke();
            ctx.setFillStyle('black');
            ctx.fillText(text, x + 15, lastY);
            lastY += spacing;
        }
        legend('green', 'Note + Flag');
        legend('red', 'Modifiers');
        legend('#999', 'Displaced Head');
        legend('#DDD', 'Formatter Shift');
        ctx.restore();
    }
}
_a = VexFlowTests;
VexFlowTests.tests = [];
VexFlowTests.RUN_CANVAS_TESTS = true;
VexFlowTests.RUN_SVG_TESTS = true;
VexFlowTests.RUN_NODE_TESTS = false;
VexFlowTests.Font = { size: 10 };
VexFlowTests.FONT_STACKS = {
    Bravura: ['Bravura', 'Academico'],
    'Finale Ash': ['Finale Ash', 'Finale Ash Text'],
    'Finale Broadway': ['Finale Broadway', 'Finale Broadway Text'],
    'Finale Maestro': ['Finale Maestro', 'Finale Maestro Text'],
    Gonville: ['GonvilleSmufl', 'Academico'],
    Gootville: ['Gootville', 'Edwin'],
    Leland: ['Leland', 'Edwin'],
    MuseJazz: ['MuseJazz', 'MuseJazz Text'],
    Petaluma: ['Petaluma', 'Petaluma Script'],
};
_VexFlowTests_NEXT_TEST_ID = { value: 0 };
export const concat = (a, b) => a.concat(b);
export const MAJOR_KEYS = [
    'C',
    'F',
    'Bb',
    'Eb',
    'Ab',
    'Db',
    'Gb',
    'Cb',
    'G',
    'D',
    'A',
    'E',
    'B',
    'F#',
    'C#',
];
export const MINOR_KEYS = [
    'Am',
    'Dm',
    'Gm',
    'Cm',
    'Fm',
    'Bbm',
    'Ebm',
    'Abm',
    'Em',
    'Bm',
    'F#m',
    'C#m',
    'G#m',
    'D#m',
    'A#m',
];
Flow.Test = VexFlowTests;
