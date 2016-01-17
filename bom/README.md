> This document contains documentation for the `tracee-bom` module.  Check the [TracEE main documentation](/README.md) to get started.

This module is a Maven BOM (Bill Of Materials). It contains all coordinates of TracEE artifacts and their version.

# Usage

Add this BOM to your parent `pom.xml`:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.tracee</groupId>
            <artifactId>tracee-bom</artifactId>
            <version>${tracee.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Now you're able to add the coordinates without a version to your application modules. 
All versions and scopes are managed by this BOM for you and if a module has been renamed or removed Apache Maven informs you in the the build lifecycle.
