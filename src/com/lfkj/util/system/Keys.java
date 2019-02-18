package com.lfkj.util.system;

import static org.jnativehook.keyboard.NativeKeyEvent.*;

/**
 * 将一堆键盘按键改成普通人看得懂的命名
 */
public enum Keys {
    ctrl(VC_CONTROL), alt(VC_ALT), shift(VC_SHIFT), win(VC_META),
    a(VC_A), b(VC_B), c(VC_C), d(VC_D), e(VC_E), f(VC_F), g(VC_G),
    h(VC_H), i(VC_I), j(VC_J), k(VC_K), l(VC_L), m(VC_M), n(VC_N),
    o(VC_O), p(VC_P), q(VC_Q), r(VC_R), s(VC_S), t(VC_T),
    u(VC_U), v(VC_V), w(VC_W), x(VC_X), y(VC_Y), z(VC_Z),
    num1(VC_1), num2(VC_2), num3(VC_3), num4(VC_4), num5(VC_5),
    num6(VC_6), num7(VC_7), num8(VC_8), num9(VC_9), num0(VC_0);

    private final int keyCode;

    public int getKeyCode() {
        return keyCode;
    }

    Keys(int keyCode) {
        this.keyCode = keyCode;
    }
}
