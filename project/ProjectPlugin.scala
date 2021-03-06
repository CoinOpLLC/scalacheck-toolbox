import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys
import scoverage.ScoverageKeys._
import sbtorgpolicies.OrgPoliciesKeys.orgBadgeListSetting
import sbtorgpolicies.OrgPoliciesPlugin
import sbtorgpolicies.OrgPoliciesPlugin.autoImport._
import sbtorgpolicies.model.scalac
import sbtorgpolicies.templates.badges._
import sbtorgpolicies.runnable.syntax._
import sbtorgpolicies.runnable._
import sbtorgpolicies.templates._
import sbtorgpolicies.templates.badges._
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import tut.TutPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = OrgPoliciesPlugin

  object autoImport {

    lazy val docsMappingsAPIDir: SettingKey[String] = settingKey[String](
      "Name of subdirectory in site target directory for api docs")

    lazy val micrositeSettings = Seq(
      micrositeName := "scalacheck-toolbox",
      micrositeDescription := "A helping hand for generating sensible data with ScalaCheck",
      micrositeDocumentationUrl := "/scalacheck-toolbox/docs/",
      micrositeBaseUrl := "/scalacheck-toolbox",
      micrositeGithubRepo := "scalacheck-toolbox",
      micrositeGithubOwner := "47deg",
      micrositePushSiteWith := GitHub4s,
      micrositeGithubToken := getEnvVar(orgGithubTokenSetting.value),
      includeFilter in Jekyll := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md",
      docsMappingsAPIDir in ScalaUnidoc := "api",
      addMappingsToSiteDir(mappings in(ScalaUnidoc, packageDoc), docsMappingsAPIDir in ScalaUnidoc)
    )

    lazy val testSettings = Seq(
      fork in Test := false
    )

    lazy val commonDeps = Seq(libraryDependencies ++= Seq(%%("scalacheck"), %("joda-time")))
  }

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      name := "scalacheck-toolbox",
      orgProjectName := "scalacheck-toolbox",
      homepage := Option(url("https://47deg.github.io/scalacheck-toolbox/")),
      description := "A helping hand for generating sensible data with ScalaCheck",
      startYear := Option(2016),
      crossScalaVersions := Seq(scalac.`2.10`, scalac.`2.11`, scalac.`2.12`),
      orgBadgeListSetting := List(
        TravisBadge.apply(_),
        CodecovBadge.apply(_),
        MavenCentralBadge.apply(_),
        LicenseBadge.apply(_),
        ScalaLangBadge.apply(_),
        GitHubIssuesBadge.apply(_)
      ),
      orgEnforcedFilesSetting := List(
        LicenseFileType(orgGithubSetting.value, orgLicenseSetting.value, startYear.value),
        ContributingFileType(
          orgProjectName.value,
          // Organization field can be configured with default value if we migrate it to the frees-io organization
          orgGithubSetting.value.copy(project = "freestyle")),
        AuthorsFileType(
          name.value,
          orgGithubSetting.value,
          orgMaintainersSetting.value,
          orgContributorsSetting.value),
        NoticeFileType(
          orgProjectName.value,
          orgGithubSetting.value,
          orgLicenseSetting.value,
          startYear.value),
        VersionSbtFileType,
        ChangelogFileType,
        ReadmeFileType(
          orgProjectName.value,
          orgGithubSetting.value,
          startYear.value,
          orgLicenseSetting.value,
          orgCommitBranchSetting.value,
          sbtPlugin.value,
          name.value,
          version.value,
          scalaBinaryVersion.value,
          sbtBinaryVersion.value,
          orgSupportedScalaJSVersion.value,
          orgBadgeListSetting.value
        )
      )
    )
}
