﻿// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

using System;
using System.Globalization;
using Org.Apache.REEF.Tang.Annotations;
using Org.Apache.REEF.Utilities.Logging;

namespace Org.Apache.REEF.Driver.Bridge
{
    // TODO[REEF-709] Act on the obsoletes here.
    public sealed class CustomTraceLevel
    {
        [Obsolete("This constructor will be made `private` after 0.13.")]
        [Inject]
        public CustomTraceLevel([Parameter(typeof(DriverBridgeConfigurationOptions.TraceLevel))] string traceLevel)
        {
            var level = Level.Verbose;
            if (Enum.TryParse(traceLevel.ToString(CultureInfo.InvariantCulture), out level))
            {
                Logger.SetCustomLevel(level);
            }
            else
            {
                Console.WriteLine("Cannot parse trace level" + traceLevel);
            }
            TraceLevel = level;
        }

        [Obsolete("The setter will be made `private` after 0.13.")]
        public Level TraceLevel { get; set; }
    }
}