const path = requrie("path");

const SOURCE = "src";
const SOURCE_PATH = path.resolve(SOURCE) + "/";

const getAbsolutePath = (
    importPath,
    filePath,
) => {
    return path
        .resolve(path.dirname(filePath), importPath)
        .replace(SOURCE_PATH, "")
};

const replaceImportPath = (j, node, filePath) =>
    j.importDeclaration(
        node.value.specifiers,
        j.literal(
            getAbsolutePath(
                node.value.source.value,
                filePath,
            )
        )
    );

export default function (file, api) {
    const j = api.jscodeshift;
    const filePath = file.path;
    return j(file.source)
        .find(j.ImportDeclaration)
        .replaceWith((node) => replaceImportPath(j, node, filePath))
        .toSource();
}