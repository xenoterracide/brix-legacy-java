# Put Project

As a developer I want to be able to create or update existing projects

## Main Success Scenario

1. Developer puts source files to be used to create the project
1. Developer puts a project configuraton in place
1. Developer runs SuD specifying the configuraton and name of project
1. SuD processes the project configuration
1. SuD puts a new file in the new project by copying the from the source file
1. SuD exits with success

## Extensions

**4a** - conflicting settings

1. handle error

**5a** - source file is template

1. Detect source file is template
1. process the template using the appropriate template processor
1. output processed template to destination location

**5a1a** - source file is binary

1. handle error

**5b** - update instructions exists

1. process instructions in order
1. update destination file in place according to instructions

**5b1a** - destination file is binary

1. handle error

**5c** - execute commands

1. execute command

### Handle Error

1. File is not created or updated
1. Throw Exception with useful message
1. log exception as error

1b - Instruction destination is binary file

1. file is not modified

1c - more than one Instruction is provided

1. Instructions are applied in order

## Technology and Data Variations List

### Configuration

Any configuraton string may be a template string and processed using the configured context.

* Configuration consists of
  * working directorory
  * skeleton configurations

* Skeleton configuration consists of
  * source
  * destination
  * overwrite boolean
  * instructions
  * context map

* Instruction consists of
  * search - a regular expression.
  * replace - a replace string, e.g. like `String#replaceAll`.
  * flags - should name as the value, allow singular or list.
  * mode - first or all, e.g. `String#replaceAll`. all should be the default.

#### Mutually exclusive keys

* source, destination, overwrite
* destination, instructions
* shell
