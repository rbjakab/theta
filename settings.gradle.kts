rootProject.name = "theta"

include(
        "analysis",
        "cfa",
        "cfa-analysis",
        "cfa-cli",
        "common",
        "core",
        "k-induction",
        "solver",
        "solver-z3",
        "sts",
        "sts-analysis",
        "sts-cli",
        "xta",
        "xta-analysis",
        "xta-cli",
        "xsts",
        "xsts-analysis",
        "xsts-cli"
)

for (project in rootProject.children) {
    val projectName = project.name
    project.projectDir = file("subprojects/$projectName")
    project.name = "${rootProject.name}-$projectName"
}
