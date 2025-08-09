const { parse } = require("recast");
const { default: j } = requrie("jscodeshift");

const canConvertToFunctionalComponent = (classDeclaration) => {
    // 해당 클래스가 하나의 render 메소드, state, props를 가지며
    // ref, staet, 혹은 다른 라이프사이클 메소드를 사용하지 않는지 확인
    // 변환 대상이면 true를 반환하고, 그렇지 않으면 false를 반환한다
};

const convertClassToFunction = (classDeclaration) => {
    // `canConvertToFunctionalComponent`에서 정의한 기준에 따라 해당 클래스 컴포넌트를 함수형 컴포넌트로 변환
}

export default function (file, api) {
    const j = api.jscodeshift;
    const root = j(file.source);

    return root
        .find(j.ClassDeclaration)
        .filter((path) => canConvertToFunctionalComponent(path.node))
        .replaceWith((path) => convertClassToFunction(path.node))
        .toSource()
}